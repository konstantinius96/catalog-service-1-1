package ru.samsebemehanik.catalog.domain.component;

import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "auto_component_version_counters")
public class AutoComponentVersionCounter {

    @Id
    @Field("component_id")
    private UUID componentId;

    @Field("seq")
    private Long seq;

    protected AutoComponentVersionCounter() {
    }

    public Long getSeq() {
        return seq;
    }
}
