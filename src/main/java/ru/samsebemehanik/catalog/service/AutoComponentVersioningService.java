package ru.samsebemehanik.catalog.service;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.samsebemehanik.catalog.domain.component.AutoComponent;
import ru.samsebemehanik.catalog.domain.component.AutoComponentVersion;
import ru.samsebemehanik.catalog.repository.AutoComponentRepository;
import ru.samsebemehanik.catalog.repository.AutoComponentVersionRepository;

@Service
public class AutoComponentVersioningService {

    private final AutoComponentRepository autoComponentRepository;
    private final AutoComponentVersionRepository autoComponentVersionRepository;

    public AutoComponentVersioningService(AutoComponentRepository autoComponentRepository,
                                          AutoComponentVersionRepository autoComponentVersionRepository) {
        this.autoComponentRepository = autoComponentRepository;
        this.autoComponentVersionRepository = autoComponentVersionRepository;
    }

    @Transactional
    public AutoComponent updateComponent(Long componentId,
                                         String description,
                                         String specification,
                                         Map<String, Object> specificationJsonb,
                                         Long changedBy) {
        AutoComponent component = autoComponentRepository.findById(componentId)
                .orElseThrow(() -> new IllegalArgumentException("Auto component not found: " + componentId));

        boolean descriptionChanged = !Objects.equals(component.getDescription(), description);
        boolean specificationChanged = !Objects.equals(component.getSpecification(), specification)
                || !Objects.equals(component.getSpecificationJsonb(), specificationJsonb);

        if (!descriptionChanged && !specificationChanged) {
            return component;
        }

        AutoComponentVersion.Snapshot snapshot = new AutoComponentVersion.Snapshot(
                component.getDescription(),
                component.getSpecificationJsonb() == null ? null : Map.copyOf(component.getSpecificationJsonb()));

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
        component.setSpecificationJsonb(specificationJsonb);

        return autoComponentRepository.save(component);
    }
}
