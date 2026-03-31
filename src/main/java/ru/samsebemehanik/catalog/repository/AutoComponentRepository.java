package ru.samsebemehanik.catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< codex/implement-search-contract-endpoint-fuxd5k
import org.springframework.data.domain.Pageable;
=======
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
>>>>>>> master
import ru.samsebemehanik.catalog.domain.component.AutoComponent;

import java.util.List;
import java.util.UUID;

public interface AutoComponentRepository extends JpaRepository<AutoComponent, UUID> {

<<<<<<< codex/implement-search-contract-endpoint-fuxd5k
    List<AutoComponent> findByNameContainingIgnoreCase(String query, Pageable pageable);

    long countByNameContainingIgnoreCase(String query);
=======
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
>>>>>>> master
}
