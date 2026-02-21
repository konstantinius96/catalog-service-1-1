package ru.samsebemehanik.catalog.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import ru.samsebemehanik.catalog.event.ComponentCreatedEvent;

@Component
public class ComponentEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(ComponentEventConsumer.class);
    private static final String COMPONENTS_TOPIC_DESTINATION = "/topic/components";

    private final SimpMessagingTemplate messagingTemplate;

    public ComponentEventConsumer(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @KafkaListener(topics = "component-events", groupId = "${spring.kafka.consumer.group-id}")
    public void onComponentCreated(ComponentCreatedEvent event) {
        log.info("Received component created event from Kafka: {}", event);
        messagingTemplate.convertAndSend(COMPONENTS_TOPIC_DESTINATION, event);
    }
}
