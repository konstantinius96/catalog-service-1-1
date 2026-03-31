package ru.samsebemehanik.catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.samsebemehanik.catalog.domain.component.AutoComponent;

import java.util.List;
import java.util.UUID;

public interface AutoComponentRepository extends JpaRepository<AutoComponent, UUID> {

    @Query(
            value = """
                    select *
                    from auto_component
                    where lower(name) like lower(concat('%', :query, '%'))
                    order by name asc
                    limit :limit offset :offset
                    """,
            nativeQuery = true
    )
    List<AutoComponent> searchByName(@Param("query") String query, @Param("limit") int limit, @Param("offset") int offset);

    @Query(
            value = """
                    select count(*)
                    from auto_component
                    where lower(name) like lower(concat('%', :query, '%'))
                    """,
            nativeQuery = true
    )
    long countByNameSearch(@Param("query") String query);
}
