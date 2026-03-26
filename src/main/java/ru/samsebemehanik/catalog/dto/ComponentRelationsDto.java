package ru.samsebemehanik.catalog.dto;

import java.util.ArrayList;
import java.util.List;

public class ComponentRelationsDto {

    private List<RelatedComponentDto> partOf;
    private List<RelatedComponentDto> hasParts;
    private List<RelatedComponentDto> interactsWith;

    public ComponentRelationsDto() {
        this.partOf = new ArrayList<>();
        this.hasParts = new ArrayList<>();
        this.interactsWith = new ArrayList<>();
    }

    public ComponentRelationsDto(List<RelatedComponentDto> partOf,
                                 List<RelatedComponentDto> hasParts,
                                 List<RelatedComponentDto> interactsWith) {
        this.partOf = partOf;
        this.hasParts = hasParts;
        this.interactsWith = interactsWith;
    }

    public List<RelatedComponentDto> getPartOf() {
        return partOf;
    }

    public List<RelatedComponentDto> getHasParts() {
        return hasParts;
    }

    public List<RelatedComponentDto> getInteractsWith() {
        return interactsWith;
    }
}
