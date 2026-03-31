package ru.samsebemehanik.catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import ru.samsebemehanik.catalog.domain.component.AutoComponent;

import java.util.List;
import java.util.UUID;

public interface AutoComponentRepository extends JpaRepository<AutoComponent, UUID> {

    List<AutoComponent> findByNameContainingIgnoreCase(String query, Pageable pageable);

    long countByNameContainingIgnoreCase(String query);
}
