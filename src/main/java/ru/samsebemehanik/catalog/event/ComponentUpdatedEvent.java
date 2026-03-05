package ru.samsebemehanik.catalog.event;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.Instant;
import java.util.UUID;

public record ComponentUpdatedEvent(
        UUID eventId,
        String eventType,
        Integer eventVersion,
        UUID componentId,
        String name,
        String description,
        String specification,
        JsonNode specificationJsonB,
        Instant changedAt
) {
}
