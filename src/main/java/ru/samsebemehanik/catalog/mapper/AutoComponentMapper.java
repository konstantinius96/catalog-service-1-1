package ru.samsebemehanik.catalog.mapper;

import ru.samsebemehanik.catalog.domain.component.AutoComponent;
import ru.samsebemehanik.catalog.dto.AutoComponentDto;

public class AutoComponentMapper {

    public static AutoComponentDto toDto(AutoComponent component) {
        return new AutoComponentDto(
                component.getId(),
                component.getName(),
                component.getDescription(),
                component.getVersion()
        );
    }
}
