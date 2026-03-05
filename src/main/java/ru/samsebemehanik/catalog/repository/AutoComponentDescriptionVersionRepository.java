package ru.samsebemehanik.catalog.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.samsebemehanik.catalog.domain.component.AutoComponentDescriptionVersion;

public interface AutoComponentDescriptionVersionRepository extends MongoRepository<AutoComponentDescriptionVersion, String> {
}
