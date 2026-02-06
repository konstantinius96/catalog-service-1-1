package ru.samsebemehanik.catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.samsebemehanik.catalog.domain.component.AutoComponent;

public interface AutoComponentRepository extends JpaRepository<AutoComponent, Long> {
}
