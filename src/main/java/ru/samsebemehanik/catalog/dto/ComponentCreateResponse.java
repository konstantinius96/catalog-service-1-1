package ru.samsebemehanik.catalog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.UUID;

public class ComponentCreateResponse {

    private UUID id;
    private String name;
    private String description;
    private String specification;

    @JsonProperty("specification_jsonB")
    private JsonNode specificationJsonB;

    public ComponentCreateResponse(UUID id, String name, String description, String specification, JsonNode specificationJsonB) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.specification = specification;
        this.specificationJsonB = specificationJsonB;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getSpecification() {
        return specification;
    }

    public JsonNode getSpecificationJsonB() {
        return specificationJsonB;
    }
}
