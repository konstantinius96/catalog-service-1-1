package ru.samsebemehanik.catalog.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaDiagnosticsConfig {

    private static final Logger log = LoggerFactory.getLogger(KafkaDiagnosticsConfig.class);

    @Bean
    public CommonErrorHandler kafkaCommonErrorHandler() {
        return new DefaultErrorHandler((record, ex) -> {
            log.error(
                    "Kafka listener failed for topic='{}', partition={}, offset={}, key={}, value={}",
                    record.topic(),
                    record.partition(),
                    record.offset(),
                    record.key(),
                    record.value(),
                    ex
            );
        }, new FixedBackOff(0L, 0L));
    }
}
