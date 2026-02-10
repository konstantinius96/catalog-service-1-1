package ru.samsebemehanik.catalog.domain.component;

import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "auto_components")
public class AutoComponent {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, columnDefinition = "text")
    private String description;

    @Column(nullable = false, columnDefinition = "text")
    private String specification;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "specification_jsonb", columnDefinition = "jsonb")
    private Map<String, Object> specificationJsonb;

    @Version
    private Long version;

    protected AutoComponent() {
    }

    public AutoComponent(String name, String description, String specification, Map<String, Object> specificationJsonb) {
        this.name = name;
        this.description = description;
        this.specification = specification;
        this.specificationJsonb = specificationJsonb;
    }

    public Long getId() {
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

    public Map<String, Object> getSpecificationJsonb() {
        return specificationJsonb;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public void setSpecificationJsonb(Map<String, Object> specificationJsonb) {
        this.specificationJsonb = specificationJsonb;
    }
 
    public Long getVersion() {
        return version;
    }
}
