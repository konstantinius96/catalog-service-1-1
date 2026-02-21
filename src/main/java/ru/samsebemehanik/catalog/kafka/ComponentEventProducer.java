package ru.samsebemehanik.catalog.kafka;

import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.samsebemehanik.catalog.domain.component.AutoComponent;
import ru.samsebemehanik.catalog.event.ComponentCreatedEvent;

@Component
public class ComponentEventProducer {

    private static final Logger log = LoggerFactory.getLogger(ComponentEventProducer.class);
    private static final String COMPONENT_EVENTS_TOPIC = "component-events";

    private final KafkaTemplate<String, ComponentCreatedEvent> kafkaTemplate;

    public ComponentEventProducer(KafkaTemplate<String, ComponentCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishComponentCreated(AutoComponent component) {
        ComponentCreatedEvent event = new ComponentCreatedEvent(
                component.getId(),
                component.getDescription(),
                Instant.now()
        );

        String messageKey = String.valueOf(component.getId());
        log.info("Publishing component created event to topic='{}', key='{}': {}",
                COMPONENT_EVENTS_TOPIC,
                messageKey,
                event);

        kafkaTemplate.send(COMPONENT_EVENTS_TOPIC, messageKey, event)
                .whenComplete(this::logDeliveryResult);
    }

    private void logDeliveryResult(SendResult<String, ComponentCreatedEvent> result, Throwable ex) {
        if (ex != null) {
            log.error("Failed to publish component created event to topic='{}'", COMPONENT_EVENTS_TOPIC, ex);
            return;
        }

        log.info(
                "Kafka publish confirmed: topic='{}', partition={}, offset={}, key='{}'",
                result.getRecordMetadata().topic(),
                result.getRecordMetadata().partition(),
                result.getRecordMetadata().offset(),
                result.getProducerRecord().key()
        );
    }
}
