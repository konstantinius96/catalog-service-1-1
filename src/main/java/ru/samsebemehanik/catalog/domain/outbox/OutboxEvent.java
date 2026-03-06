package ru.samsebemehanik.catalog.domain.outbox;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "outbox_event")
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "aggregate_type", nullable = false, length = 100)
    private String aggregateType;

    @Column(name = "aggregate_id", nullable = false, length = 100)
    private String aggregateId;

    @Column(name = "topic", nullable = false, length = 255)
    private String topic;

    @Column(name = "event_key", nullable = false, length = 255)
    private String eventKey;

    @Column(name = "event_type", nullable = false, length = 255)
    private String eventType;

    @Lob
    @Column(name = "payload", nullable = false)
    private String payload;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "published_at")
    private Instant publishedAt;

    @Column(name = "retry_count", nullable = false)
    private Integer retryCount;

    @Column(name = "last_error")
    private String lastError;

    protected OutboxEvent() {
    }

    public OutboxEvent(String aggregateType,
                       String aggregateId,
                       String topic,
                       String eventKey,
                       String eventType,
                       String payload,
                       Instant createdAt) {
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.topic = topic;
        this.eventKey = eventKey;
        this.eventType = eventType;
        this.payload = payload;
        this.createdAt = createdAt;
        this.retryCount = 0;
    }

    public Long getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }

    public String getEventKey() {
        return eventKey;
    }

    public String getPayload() {
        return payload;
    }

    public String getEventType() {
        return eventType;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public String getLastError() {
        return lastError;
    }

    public void markPublished(Instant publishedAt) {
        this.publishedAt = publishedAt;
        this.lastError = null;
    }

    public void markFailed(String errorMessage) {
        this.retryCount = this.retryCount + 1;
        this.lastError = errorMessage;
    }
}
