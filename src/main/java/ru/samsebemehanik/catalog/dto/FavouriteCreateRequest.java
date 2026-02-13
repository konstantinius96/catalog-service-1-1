package ru.samsebemehanik.catalog.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class FavouriteCreateRequest {

    @NotNull
    private Long userId;

    @NotNull
    private UUID autoComponentId;

    public FavouriteCreateRequest() {
    }

    public FavouriteCreateRequest(Long userId, UUID autoComponentId) {
        this.userId = userId;
        this.autoComponentId = autoComponentId;
    }

    public Long getUserId() {
        return userId;
    }

    public UUID getAutoComponentId() {
        return autoComponentId;
    }
}
