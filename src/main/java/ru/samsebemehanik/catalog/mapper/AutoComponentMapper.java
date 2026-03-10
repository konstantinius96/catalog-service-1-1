package ru.samsebemehanik.catalog.mapper;

import ru.samsebemehanik.catalog.domain.component.AutoComponent;
import ru.samsebemehanik.catalog.dto.AutoComponentDto;
import ru.samsebemehanik.catalog.dto.ComponentCreateResponse;
import ru.samsebemehanik.catalog.dto.ComponentEditResponse;
import ru.samsebemehanik.catalog.dto.MenuResponse;

public class AutoComponentMapper {

    public static AutoComponentDto toDto(AutoComponent component) {
        return new AutoComponentDto(
                component.getId(),
                component.getName(),
                component.getDescription(),
                component.getSpecification(),
                component.getSpecificationJsonB()
        );
    }

    public static MenuResponse toMenuResponse(AutoComponent component) {
        return new MenuResponse(
                component.getId(),
                component.getName()
        );
    }

    public static ComponentCreateResponse toCreateResponse(AutoComponent component) {
        return new ComponentCreateResponse(
                component.getId(),
                component.getName(),
                component.getDescription(),
                component.getSpecification(),
                component.getSpecificationJsonB()
        );
    }

    public static ComponentEditResponse toEditResponse(AutoComponent component) {
        return new ComponentEditResponse(
                component.getId(),
                component.getName(),
                component.getDescription(),
                component.getSpecification(),
                component.getSpecificationJsonB()
        );
    }
}
