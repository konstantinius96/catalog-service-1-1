package ru.samsebemehanik.catalog.dto;

import java.util.UUID;

public class RelatedComponentDto {

    private UUID id;
    private String name;

    public RelatedComponentDto(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
