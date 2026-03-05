package ru.samsebemehanik.catalog.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Service;
import ru.samsebemehanik.catalog.domain.outbox.OutboxEvent;
import ru.samsebemehanik.catalog.event.ComponentUpdatedEvent;
import ru.samsebemehanik.catalog.exception.ComponentProcessingException;
import ru.samsebemehanik.catalog.repository.OutboxEventRepository;

@Service
public class OutboxService {

    private static final String TOPIC_COMPONENT_UPDATED = "auto-component-updated";

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    public OutboxService(OutboxEventRepository outboxEventRepository, ObjectMapper objectMapper) {
        this.outboxEventRepository = outboxEventRepository;
        this.objectMapper = objectMapper;
    }

    public void storeComponentUpdated(AutoComponentStateSnapshot componentState) {
        ComponentUpdatedEvent event = new ComponentUpdatedEvent(
                UUID.randomUUID(),
                "ComponentUpdatedEvent",
                1,
                componentState.id(),
                componentState.name(),
                componentState.description(),
                componentState.specification(),
                componentState.specificationJsonB(),
                Instant.now()
        );

        String payload;
        try {
            payload = objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException ex) {
            throw new ComponentProcessingException("Failed to serialize outbox event", ex);
        }

        OutboxEvent outboxEvent = new OutboxEvent(
                "auto_component",
                componentState.id().toString(),
                TOPIC_COMPONENT_UPDATED,
                componentState.id().toString(),
                event.eventType(),
                payload,
                Instant.now()
        );

        outboxEventRepository.save(outboxEvent);
    }

    public record AutoComponentStateSnapshot(
            UUID id,
            String name,
            String description,
            String specification,
            com.fasterxml.jackson.databind.JsonNode specificationJsonB
    ) {
    }
}
