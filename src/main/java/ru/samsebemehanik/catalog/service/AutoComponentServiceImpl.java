package ru.samsebemehanik.catalog.service;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import ru.samsebemehanik.catalog.exception.ComponentNotFoundException;
import ru.samsebemehanik.catalog.kafka.ComponentEventProducer;
import ru.samsebemehanik.catalog.mapper.AutoComponentMapper;
import ru.samsebemehanik.catalog.repository.AutoComponentRepository;
import ru.samsebemehanik.catalog.service.OutboxService.AutoComponentStateSnapshot;

@Service
public class AutoComponentServiceImpl implements AutoComponentService {

    private final AutoComponentRepository autoComponentRepository;
    private final ComponentEventProducer componentEventProducer;
    private final OutboxService outboxService;

    public AutoComponentServiceImpl(
            AutoComponentRepository autoComponentRepository,
            ComponentEventProducer componentEventProducer,
            OutboxService outboxService
    ) {
        this.autoComponentRepository = autoComponentRepository;
        this.componentEventProducer = componentEventProducer;
        this.outboxService = outboxService;
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

        return AutoComponentMapper.toDto(component);
    }
    @Override
    @Transactional(readOnly = true)
    public AutoComponentPageResponse getAll(int page, int size) {
        Page<AutoComponent> componentPage = autoComponentRepository.findAll(PageRequest.of(page, size));

        return new AutoComponentPageResponse(
                componentPage.getContent().stream()
                        .map(AutoComponentMapper::toDto)
                        .toList(),
                componentPage.getNumber(),
                componentPage.getSize(),
                componentPage.getTotalElements(),
                componentPage.getTotalPages()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuResponse> getLeftMenu() {
        return autoComponentRepository.findAll().stream()
                .map(AutoComponentMapper::toMenuResponse)
                .toList();
    }

}
