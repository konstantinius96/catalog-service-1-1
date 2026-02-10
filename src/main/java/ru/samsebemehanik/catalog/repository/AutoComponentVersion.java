package ru.samsebemehanik.catalog.domain.component;

import java.time.Instant;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "auto_component_versions")
public class AutoComponentVersion {

    @Id
    private String id;
    private Long componentId;
    private Long versionNumber;
    private Instant changedAt;
    private Long changedBy;
    private Snapshot snapshot;

    public AutoComponentVersion(Long componentId,
                                Long versionNumber,
                                Instant changedAt,
                                Long changedBy,
                                Snapshot snapshot) {
        this.componentId = componentId;
        this.versionNumber = versionNumber;
        this.changedAt = changedAt;
        this.changedBy = changedBy;
        this.snapshot = snapshot;
    }

    protected AutoComponentVersion() {
    }

    public String getId() {
        return id;
    }

    public Long getComponentId() {
        return componentId;
    }

    public Long getVersionNumber() {
        return versionNumber;
    }

    public Instant getChangedAt() {
        return changedAt;
    }

    public Long getChangedBy() {
        return changedBy;
    }

    public Snapshot getSnapshot() {
        return snapshot;
    }

    public static class Snapshot {
        private String description;
        private Map<String, Object> specification;

        public Snapshot(String description, Map<String, Object> specification) {
            this.description = description;
            this.specification = specification;
        }

        protected Snapshot() {
        }

        public String getDescription() {
            return description;
        }

        public Map<String, Object> getSpecification() {
            return specification;
        }
    }
}
