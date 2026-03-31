package ru.samsebemehanik.catalog.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.samsebemehanik.catalog.domain.component.AutoComponent;
import ru.samsebemehanik.catalog.dto.AutoComponentPageResponse;
import ru.samsebemehanik.catalog.dto.ComponentCreateRequest;
import ru.samsebemehanik.catalog.dto.ComponentCreateResponse;
import ru.samsebemehanik.catalog.dto.ComponentEditRequest;
import ru.samsebemehanik.catalog.dto.ComponentEditResponse;
import ru.samsebemehanik.catalog.dto.MenuResponse;
import ru.samsebemehanik.catalog.dto.AutoComponentDto;
import ru.samsebemehanik.catalog.dto.ComponentRelationsDto;
import ru.samsebemehanik.catalog.dto.RelatedComponentDto;
import ru.samsebemehanik.catalog.dto.SearchItem;
import ru.samsebemehanik.catalog.dto.SearchResponse;
import ru.samsebemehanik.catalog.exception.ComponentNotFoundException;
import ru.samsebemehanik.catalog.kafka.ComponentEventProducer;
import ru.samsebemehanik.catalog.mapper.AutoComponentMapper;
import ru.samsebemehanik.catalog.repository.AutoComponentRepository;
import ru.samsebemehanik.catalog.repository.ComponentRelationsViewRepository;
import ru.samsebemehanik.catalog.repository.OffsetLimitPageable;
import ru.samsebemehanik.catalog.repository.projection.ComponentRelationFullRow;
import ru.samsebemehanik.catalog.service.OutboxService.AutoComponentStateSnapshot;

@Service
public class AutoComponentServiceImpl implements AutoComponentService {

    private final AutoComponentRepository autoComponentRepository;
    private final ComponentEventProducer componentEventProducer;
    private final OutboxService outboxService;
    private final ComponentRelationsViewRepository componentRelationsViewRepository;

    public AutoComponentServiceImpl(
            AutoComponentRepository autoComponentRepository,
            ComponentEventProducer componentEventProducer,
            OutboxService outboxService,
            ComponentRelationsViewRepository componentRelationsViewRepository
    ) {
        this.autoComponentRepository = autoComponentRepository;
        this.componentEventProducer = componentEventProducer;
        this.outboxService = outboxService;
        this.componentRelationsViewRepository = componentRelationsViewRepository;
    }

    @Override
    @Transactional
    public ComponentCreateResponse create(ComponentCreateRequest request) {
        AutoComponent component = new AutoComponent(
                request.getName(),
                request.getDescription(),
                request.getSpecification(),
                request.getSpecificationJsonB()
        );

        AutoComponent saved = autoComponentRepository.save(component);
        componentEventProducer.publishComponentCreated(saved);
        return AutoComponentMapper.toCreateResponse(saved);
    }

    @Override
    @Transactional
    public ComponentEditResponse edit(UUID id, ComponentEditRequest request) {
        AutoComponent component = autoComponentRepository.findById(id)
                .orElseThrow(() -> new ComponentNotFoundException("Component with id=" + id + " was not found"));

        component.setName(request.getName());
        component.setDescription(request.getDescription());
        component.setSpecification(request.getSpecification());
        component.setSpecificationJsonB(request.getSpecificationJsonB());

        autoComponentRepository.saveAndFlush(component);
        AutoComponent factualState = autoComponentRepository.findById(id)
                .orElseThrow(() -> new ComponentNotFoundException("Component with id=" + id + " was not found"));

        outboxService.storeComponentUpdated(new AutoComponentStateSnapshot(
                factualState.getId(),
                factualState.getName(),
                factualState.getDescription(),
                factualState.getSpecification(),
                factualState.getSpecificationJsonB()
        ));

        return AutoComponentMapper.toEditResponse(factualState);
    }

    @Override
    @Transactional(readOnly = true)
    public AutoComponentDto getById(UUID id) {
        AutoComponent component = autoComponentRepository.findById(id)
                .orElseThrow(() -> new ComponentNotFoundException("Component with id=" + id + " was not found"));

        ComponentRelationsDto relations = mapRelations(id);
        return AutoComponentMapper.toDto(component, relations);
    }

    private ComponentRelationsDto mapRelations(UUID componentId) {
        List<ComponentRelationFullRow> relationRows = componentRelationsViewRepository.findByComponentId(componentId);

        List<RelatedComponentDto> partOf = new ArrayList<>();
        List<RelatedComponentDto> hasParts = new ArrayList<>();
        List<RelatedComponentDto> interactsWith = new ArrayList<>();

        Set<UUID> partOfIds = new LinkedHashSet<>();
        Set<UUID> hasPartIds = new LinkedHashSet<>();
        Set<UUID> interactsWithIds = new LinkedHashSet<>();

        for (ComponentRelationFullRow row : relationRows) {
            if ("PART_OF".equals(row.getRelationType())) {
                if (componentId.equals(row.getFromId()) && partOfIds.add(row.getToId())) {
                    partOf.add(new RelatedComponentDto(row.getToId(), row.getToName()));
                }
                if (componentId.equals(row.getToId()) && hasPartIds.add(row.getFromId())) {
                    hasParts.add(new RelatedComponentDto(row.getFromId(), row.getFromName()));
                }
            }

            if ("INTERACTS_WITH".equals(row.getRelationType())
                    && componentId.equals(row.getFromId())
                    && interactsWithIds.add(row.getToId())) {
                interactsWith.add(new RelatedComponentDto(row.getToId(), row.getToName()));
            }
        }

        return new ComponentRelationsDto(partOf, hasParts, interactsWith);
    }

    @Override
    @Transactional(readOnly = true)
    public AutoComponentPageResponse getAll(int page, int size) {
        int normalizedPage = page < 1 ? 1 : page;
        int normalizedSize = size < 1 ? 20 : size;

        Page<AutoComponent> componentPage = autoComponentRepository.findAll(
                PageRequest.of(normalizedPage - 1, normalizedSize)
        );

        return new AutoComponentPageResponse(
                componentPage.getContent().stream()
                        .map(AutoComponentMapper::toDto)
                        .toList(),
                normalizedPage,
                normalizedSize,
                componentPage.getTotalElements(),
                componentPage.getTotalPages()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public SearchResponse searchByName(String query, int limit, int offset) {
        String normalizedQuery = query == null ? "" : query.trim();
        if (normalizedQuery.length() < 2) {
            throw new IllegalArgumentException("query must contain at least 2 characters");
        }
        if (limit < 1 || limit > 50) {
            throw new IllegalArgumentException("limit must be between 1 and 50");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("offset must be greater than or equal to 0");
        }

<<<<<<< codex/implement-search-contract-endpoint-fuxd5k
        long total = autoComponentRepository.countByNameContainingIgnoreCase(normalizedQuery);
        OffsetLimitPageable pageable = new OffsetLimitPageable(offset, limit, Sort.by(Sort.Direction.ASC, "name"));
        List<SearchItem> items = autoComponentRepository.findByNameContainingIgnoreCase(normalizedQuery, pageable).stream()
=======
        long total = autoComponentRepository.countByNameSearch(normalizedQuery);
        List<SearchItem> items = autoComponentRepository.searchByName(normalizedQuery, limit, offset).stream()
>>>>>>> master
                .map(component -> new SearchItem(component.getId(), component.getName(), component.getDescription()))
                .toList();

        boolean hasMore = total > (long) offset + items.size();
        return new SearchResponse(total, hasMore, items);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuResponse> getLeftMenu() {
        return autoComponentRepository.findAll().stream()
                .map(AutoComponentMapper::toMenuResponse)
                .toList();
    }

}
