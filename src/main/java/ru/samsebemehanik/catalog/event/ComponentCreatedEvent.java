package ru.samsebemehanik.catalog.event;

import java.time.Instant;
import java.util.UUID;

public record ComponentCreatedEvent(
        UUID componentId,
        String description,
        Instant createdAt
) {
}
