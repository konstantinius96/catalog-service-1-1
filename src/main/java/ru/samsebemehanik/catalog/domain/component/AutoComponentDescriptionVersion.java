package ru.samsebemehanik.catalog.domain.component;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "auto_component_description_version")
@CompoundIndexes({
        @CompoundIndex(name = "ux_component_version", def = "{'component_id': 1, 'version_number': 1}", unique = true),
        @CompoundIndex(name = "ux_component_event", def = "{'component_id': 1, 'event_id': 1}", unique = true)
})
public class AutoComponentDescriptionVersion {

    @Id
    private String id;

    @Field("component_id")
    private UUID componentId;

    @Field("version_number")
    private Long versionNumber;

    @Field("changed_at")
    private Instant changedAt;

    @Field("changed_by")
    private Long changedBy;

    @Field("event_id")
    private UUID eventId;

    @Field("snapshot")
    private Snapshot snapshot;

    protected AutoComponentDescriptionVersion() {
    }

    public AutoComponentDescriptionVersion(UUID componentId,
                                           Long versionNumber,
                                           Instant changedAt,
                                           Long changedBy,
                                           UUID eventId,
                                           Snapshot snapshot) {
        this.componentId = componentId;
        this.versionNumber = versionNumber;
        this.changedAt = changedAt;
        this.changedBy = changedBy;
        this.eventId = eventId;
        this.snapshot = snapshot;
    }

    public static class Snapshot {

        @Field("name")
        private String name;

        @Field("description")
        private String description;

        @Field("specification")
        private String specification;

        @Field("specification_jsonB")
        private JsonNode specificationJsonB;

        protected Snapshot() {
        }

        public Snapshot(String name, String description, String specification, JsonNode specificationJsonB) {
            this.name = name;
            this.description = description;
            this.specification = specification;
            this.specificationJsonB = specificationJsonB;
        }
    }
}
