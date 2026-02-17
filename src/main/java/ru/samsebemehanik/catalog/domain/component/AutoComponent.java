package ru.samsebemehanik.catalog.domain.component;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Type;

import java.util.UUID;

@Entity
@Table(name = "auto_component")
public class AutoComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column(columnDefinition = "text")
    private String specification;

    @Type(JsonBinaryType.class)
    @Column(name = "specification_jsonb", columnDefinition = "jsonb")
    private JsonNode specificationJsonB;

    protected AutoComponent() {
    }

    public AutoComponent(String name, String description, String specification, JsonNode specificationJsonB) {
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

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public void setSpecificationJsonB(JsonNode specificationJsonB) {
        this.specificationJsonB = specificationJsonB;
    }

}
