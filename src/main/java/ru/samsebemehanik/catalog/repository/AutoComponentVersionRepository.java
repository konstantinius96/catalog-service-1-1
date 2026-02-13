package ru.samsebemehanik.catalog.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.samsebemehanik.catalog.domain.component.AutoComponentVersion;

public interface AutoComponentVersionRepository extends MongoRepository<AutoComponentVersion, String> {

    Optional<AutoComponentVersion> findTopByComponentIdOrderByVersionNumberDesc(UUID componentId);
}
