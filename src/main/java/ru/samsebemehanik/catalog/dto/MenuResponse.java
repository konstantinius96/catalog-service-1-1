package ru.samsebemehanik.catalog.dto;

import java.util.UUID;

public class MenuResponse {

    private UUID id;
    private String name;

    public MenuResponse(UUID id, String name) {
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
