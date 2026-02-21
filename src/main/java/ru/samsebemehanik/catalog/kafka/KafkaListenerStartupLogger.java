package ru.samsebemehanik.catalog.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class KafkaListenerStartupLogger {

    private static final Logger log = LoggerFactory.getLogger(KafkaListenerStartupLogger.class);

    private final KafkaListenerEndpointRegistry registry;

    public KafkaListenerStartupLogger(KafkaListenerEndpointRegistry registry) {
        this.registry = registry;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void logKafkaListenerContainers() {
        if (registry.getListenerContainers().isEmpty()) {
            log.warn("No Kafka listener containers registered. Check @KafkaListener scanning/configuration.");
            return;
        }

        for (MessageListenerContainer container : registry.getListenerContainers()) {
            log.info("Kafka listener container='{}', groupId='{}', running={}, assignedPartitions={}",
                    container,
                    container.getContainerProperties().getGroupId(),
                    container.isRunning(),
                    container.getAssignedPartitions());
        }
    }
}
