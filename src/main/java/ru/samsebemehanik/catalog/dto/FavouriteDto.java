package ru.samsebemehanik.catalog.dto;

import java.time.LocalDateTime;

public class FavouriteDto {

    private Long id;
    private Long userId;
    private AutoComponentDto component;
    private LocalDateTime addedAt;
    private Long version;

    public FavouriteDto(Long id, Long userId, AutoComponentDto component, LocalDateTime addedAt, Long version) {
        this.id = id;
        this.userId = userId;
        this.component = component;
        this.addedAt = addedAt;
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public AutoComponentDto getComponent() {
        return component;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public Long getVersion() {
        return version;
    }
}
