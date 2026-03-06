package ru.samsebemehanik.catalog.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.samsebemehanik.catalog.domain.outbox.OutboxEvent;
import ru.samsebemehanik.catalog.event.ComponentUpdatedEvent;
import ru.samsebemehanik.catalog.repository.OutboxEventRepository;

@Component
public class OutboxRelayPublisher {

    private static final Logger log = LoggerFactory.getLogger(OutboxRelayPublisher.class);

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, ComponentUpdatedEvent> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private boolean outboxStorageWarned;

    public OutboxRelayPublisher(OutboxEventRepository outboxEventRepository,
                                KafkaTemplate<String, ComponentUpdatedEvent> kafkaTemplate,
                                ObjectMapper objectMapper) {
        this.outboxEventRepository = outboxEventRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedDelayString = "${app.outbox.relay.fixed-delay-ms:3000}")
    @Transactional
    public void publishPendingEvents() {
        List<OutboxEvent> events;
        try {
            events = outboxEventRepository.findTop100ByPublishedAtIsNullOrderByIdAsc();
            outboxStorageWarned = false;
        } catch (DataAccessException ex) {
            if (!outboxStorageWarned) {
                outboxStorageWarned = true;
                log.warn("Outbox table is unavailable. Apply DB migration for table 'outbox_event'. " +
                        "Relay is temporarily skipped until schema is ready.", ex);
            } else {
                log.debug("Outbox table is still unavailable, relay skipped.");
            }
            return;
        }

        for (OutboxEvent outboxEvent : events) {
            publishSingle(outboxEvent);
        }
    }

    private void publishSingle(OutboxEvent outboxEvent) {
        try {
            ComponentUpdatedEvent event = objectMapper.readValue(outboxEvent.getPayload(), ComponentUpdatedEvent.class);
            kafkaTemplate.send(outboxEvent.getTopic(), outboxEvent.getEventKey(), event)
                    .get(10, TimeUnit.SECONDS);

            outboxEvent.markPublished(Instant.now());
            log.info("Outbox event id={} published to topic='{}' key='{}'",
                    outboxEvent.getId(), outboxEvent.getTopic(), outboxEvent.getEventKey());
        } catch (ExecutionException | TimeoutException | InterruptedException ex) {
            if (ex instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            outboxEvent.markFailed(ex.getMessage());
            log.error("Failed to publish outbox event id={} (retryCount={})",
                    outboxEvent.getId(), outboxEvent.getRetryCount(), ex);
        } catch (Exception ex) {
            outboxEvent.markFailed(ex.getMessage());
            log.error("Outbox event id={} payload processing failed", outboxEvent.getId(), ex);
        }
    }
}
