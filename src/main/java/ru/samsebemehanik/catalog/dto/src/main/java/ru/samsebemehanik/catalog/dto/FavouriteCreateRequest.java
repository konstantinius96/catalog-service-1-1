package ru.samsebemehanik.catalog.dto;

import jakarta.validation.constraints.NotNull;

public class FavouriteCreateRequest {

    @NotNull
    private Long userId;

    @NotNull
    private Long autoComponentId;

    public FavouriteCreateRequest() {
    }

    public FavouriteCreateRequest(Long userId, Long autoComponentId) {
        this.userId = userId;
        this.autoComponentId = autoComponentId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getAutoComponentId() {
        return autoComponentId;
    }
}
