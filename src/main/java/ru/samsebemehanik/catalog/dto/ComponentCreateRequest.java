package ru.samsebemehanik.catalog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotNull;

public class ComponentCreateRequest {

    @NotNull
    private String name;
    private String description;
    private String specification;

    @JsonProperty("specification_jsonB")
    private JsonNode specificationJsonB;

    public ComponentCreateRequest() {
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
