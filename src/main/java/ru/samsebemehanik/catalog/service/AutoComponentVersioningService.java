package ru.samsebemehanik.catalog.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.samsebemehanik.catalog.domain.component.AutoComponent;
import ru.samsebemehanik.catalog.domain.component.AutoComponentVersion;
import ru.samsebemehanik.catalog.repository.AutoComponentRepository;
import ru.samsebemehanik.catalog.repository.AutoComponentVersionRepository;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class AutoComponentVersioningService {

    private final AutoComponentRepository autoComponentRepository;
    private final AutoComponentVersionRepository autoComponentVersionRepository;
    private final ObjectMapper objectMapper;

    public AutoComponentVersioningService(AutoComponentRepository autoComponentRepository,
                                          AutoComponentVersionRepository autoComponentVersionRepository,
                                          ObjectMapper objectMapper) {
        this.autoComponentRepository = autoComponentRepository;
        this.autoComponentVersionRepository = autoComponentVersionRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public AutoComponent updateComponent(UUID componentId,
                                         String description,
                                         String specification,
                                         Map<String, Object> specificationJsonb,
                                         Long changedBy) {
        AutoComponent component = autoComponentRepository.findById(componentId)
                .orElseThrow(() -> new IllegalArgumentException("Auto component not found: " + componentId));

        boolean descriptionChanged = !Objects.equals(component.getDescription(), description);
        boolean specificationChanged = !Objects.equals(component.getSpecification(), specification)
                || !Objects.equals(component.getSpecificationJsonB(), objectMapper.valueToTree(specificationJsonb));

        if (!descriptionChanged && !specificationChanged) {
            return component;
        }

        Map<String, Object> currentJson = component.getSpecificationJsonB() == null
                ? null
                : objectMapper.convertValue(component.getSpecificationJsonB(), new TypeReference<>() {
                });

        AutoComponentVersion.Snapshot snapshot = new AutoComponentVersion.Snapshot(
                component.getDescription(),
                currentJson == null ? null : Map.copyOf(currentJson));

        long nextVersion = autoComponentVersionRepository
                .findTopByComponentIdOrderByVersionNumberDesc(componentId)
                .map(AutoComponentVersion::getVersionNumber)
                .map(versionNumber -> versionNumber + 1)
                .orElse(1L);

        autoComponentVersionRepository.save(new AutoComponentVersion(
                componentId,
                nextVersion,
                Instant.now(),
                changedBy,
                snapshot));

        component.setDescription(description);
        component.setSpecification(specification);
        component.setSpecificationJsonB(objectMapper.valueToTree(specificationJsonb));

        return autoComponentRepository.save(component);
    }
}
