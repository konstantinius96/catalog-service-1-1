package ru.samsebemehanik.catalog.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.samsebemehanik.catalog.domain.component.AutoComponentDescriptionVersion;
import ru.samsebemehanik.catalog.domain.component.AutoComponentVersionCounter;
import ru.samsebemehanik.catalog.event.ComponentUpdatedEvent;
import ru.samsebemehanik.catalog.repository.AutoComponentDescriptionVersionRepository;

@Service
public class ComponentVersionService {

    private static final Logger log = LoggerFactory.getLogger(ComponentVersionService.class);

    private final MongoTemplate mongoTemplate;
    private final AutoComponentDescriptionVersionRepository versionRepository;

    public ComponentVersionService(MongoTemplate mongoTemplate,
                                   AutoComponentDescriptionVersionRepository versionRepository) {
        this.mongoTemplate = mongoTemplate;
        this.versionRepository = versionRepository;
    }

    @KafkaListener(topics = "auto-component-updated", groupId = "${spring.kafka.consumer.group-id}")
    public void onComponentUpdated(ComponentUpdatedEvent event) {
        Long versionNumber = nextVersion(event.componentId());

        AutoComponentDescriptionVersion version = new AutoComponentDescriptionVersion(
                event.componentId(),
                versionNumber,
                event.changedAt(),
                null,
                event.eventId(),
                new AutoComponentDescriptionVersion.Snapshot(
                        event.name(),
                        event.description(),
                        event.specification(),
                        event.specificationJsonB()
                )
        );

        try {
            versionRepository.save(version);
            log.info("Version saved for componentId={}, versionNumber={}, eventId={}",
                    event.componentId(), versionNumber, event.eventId());
        } catch (DuplicateKeyException ex) {
            log.info("Duplicate component update event ignored for componentId={}, eventId={}",
                    event.componentId(), event.eventId());
        }
    }

    private Long nextVersion(java.util.UUID componentId) {
        Query query = new Query(Criteria.where("component_id").is(componentId));
        Update update = new Update().inc("seq", 1L);

        AutoComponentVersionCounter counter = mongoTemplate.findAndModify(
                query,
                update,
                FindAndModifyOptions.options().returnNew(true).upsert(true),
                AutoComponentVersionCounter.class,
                "auto_component_version_counters"
        );

        if (counter == null || counter.getSeq() == null) {
            throw new IllegalStateException("Failed to increment MongoDB version counter for componentId=" + componentId);
        }

        return counter.getSeq();
    }
}
