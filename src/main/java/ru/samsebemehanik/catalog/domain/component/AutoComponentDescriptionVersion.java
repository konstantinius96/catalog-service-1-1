package ru.samsebemehanik.catalog.domain.component;

import java.time.Instant;
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
    private String componentId;

    @Field("version_number")
    private Long versionNumber;

    @Field("changed_at")
    private Instant changedAt;

    @Field("changed_by")
    private Long changedBy;

    @Field("event_id")
    private String eventId;

    @Field("snapshot")
    private Snapshot snapshot;

    protected AutoComponentDescriptionVersion() {
    }

    public AutoComponentDescriptionVersion(String componentId,
                                           Long versionNumber,
                                           Instant changedAt,
                                           Long changedBy,
                                           String eventId,
                                           Snapshot snapshot) {
        this.componentId = componentId;
        this.versionNumber = versionNumber;
        this.changedAt = changedAt;
        this.changedBy = changedBy;
        this.eventId = eventId;
        this.snapshot = snapshot;
    }

    public String getId() {
        return id;
    }

    public static class Snapshot {

        @Field("name")
        private String name;

        @Field("description")
        private String description;

        @Field("specification")
        private String specification;

        @Field("specification_jsonB")
        private Object specificationJsonB;

        protected Snapshot() {
        }

        public Snapshot(String name, String description, String specification, Object specificationJsonB) {
            this.name = name;
            this.description = description;
            this.specification = specification;
            this.specificationJsonB = specificationJsonB;
        }
    }
}
