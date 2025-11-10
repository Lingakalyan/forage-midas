package com.jpmc.midascore.config;

import com.jpmc.midascore.foundation.Transaction;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, Transaction> transactionConsumerFactory(KafkaProperties props) {
        // Use Boot’s effective consumer props (Embedded Kafka injects bootstrap)
        Map<String, Object> cfg = props.buildConsumerProperties();

        // Configure JSON VALUE deserialization via properties only (no explicit instance)
        cfg.put(JsonDeserializer.TRUSTED_PACKAGES, "com.jpmc.midascore.foundation");
        cfg.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        cfg.put(JsonDeserializer.VALUE_DEFAULT_TYPE, Transaction.class.getName());

        return new DefaultKafkaConsumerFactory<>(cfg);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Transaction>
    transactionKafkaListenerContainerFactory(ConsumerFactory<String, Transaction> cf) {
        ConcurrentKafkaListenerContainerFactory<String, Transaction> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(cf);
        factory.setBatchListener(false);
        return factory;
    }
}