package ru.samsebemehanik.catalog.domain.component;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "auto_component_version_counters")
public class AutoComponentVersionCounter {

    @Id
    private String componentId;

    @Field("seq")
    private Long seq;

    protected AutoComponentVersionCounter() {
    }

    public Long getSeq() {
        return seq;
    }
}
