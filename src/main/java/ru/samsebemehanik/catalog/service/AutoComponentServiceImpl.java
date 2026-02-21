package ru.samsebemehanik.catalog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.samsebemehanik.catalog.domain.component.AutoComponent;
import ru.samsebemehanik.catalog.dto.ComponentCreateRequest;
import ru.samsebemehanik.catalog.dto.ComponentCreateResponse;
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
}
