package ru.samsebemehanik.catalog.mapper;

import ru.samsebemehanik.catalog.domain.component.AutoComponent;
import ru.samsebemehanik.catalog.dto.AutoComponentDto;
import ru.samsebemehanik.catalog.dto.ComponentCreateResponse;

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

    public static ComponentCreateResponse toCreateResponse(AutoComponent component) {
        return new ComponentCreateResponse(
                component.getId(),
                component.getName(),
                component.getDescription(),
                component.getSpecification(),
                component.getSpecificationJsonB()
        );
    }
}
