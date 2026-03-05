package ru.samsebemehanik.catalog.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.samsebemehanik.catalog.domain.outbox.OutboxEvent;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {

    List<OutboxEvent> findTop100ByPublishedAtIsNullOrderByIdAsc();
}
