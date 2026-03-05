package ru.samsebemehanik.catalog.service;

import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.samsebemehanik.catalog.domain.component.AutoComponent;
import ru.samsebemehanik.catalog.dto.ComponentCreateRequest;
import ru.samsebemehanik.catalog.dto.ComponentCreateResponse;
import ru.samsebemehanik.catalog.dto.ComponentEditRequest;
import ru.samsebemehanik.catalog.dto.ComponentEditResponse;
import ru.samsebemehanik.catalog.exception.ComponentNotFoundException;
import ru.samsebemehanik.catalog.kafka.ComponentEventProducer;
import ru.samsebemehanik.catalog.mapper.AutoComponentMapper;
import ru.samsebemehanik.catalog.repository.AutoComponentRepository;

@Service
public class AutoComponentServiceImpl implements AutoComponentService {

    private final AutoComponentRepository autoComponentRepository;
    private final ComponentEventProducer componentEventProducer;

    public AutoComponentServiceImpl(
            AutoComponentRepository autoComponentRepository,
            ComponentEventProducer componentEventProducer
    ) {
        this.autoComponentRepository = autoComponentRepository;
        this.componentEventProducer = componentEventProducer;
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

        AutoComponent updated = autoComponentRepository.save(component);
        return AutoComponentMapper.toEditResponse(updated);
    }
}
