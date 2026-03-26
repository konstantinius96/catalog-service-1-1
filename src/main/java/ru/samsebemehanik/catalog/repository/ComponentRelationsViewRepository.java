package ru.samsebemehanik.catalog.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import ru.samsebemehanik.catalog.repository.projection.ComponentRelationFullRow;

public interface ComponentRelationsViewRepository extends Repository<Object, UUID> {

    @Query(value = """
            SELECT
                relation_type AS relationType,
                from_id AS fromId,
                from_name AS fromName,
                to_id AS toId,
                to_name AS toName
            FROM component_relations_full
            WHERE from_id = :componentId OR to_id = :componentId
            """, nativeQuery = true)
    List<ComponentRelationFullRow> findByComponentId(@Param("componentId") UUID componentId);
}
