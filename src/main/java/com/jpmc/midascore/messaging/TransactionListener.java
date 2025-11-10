package com.jpmc.midascore.messaging;

import com.jpmc.midascore.model.Transaction; // <-- use the actual Transaction class path
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class TransactionListener {

    private static final Logger log = LoggerFactory.getLogger(TransactionListener.class);

    // The topic name comes from application.yml: general.kafka-topic
    @KafkaListener(
        topics = "${general.kafka-topic}",
        groupId = "midas-core",
        containerFactory = "transactionKafkaListenerContainerFactory"
    )
    public void onTransaction(
            @Payload Transaction tx,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.OFFSET) long offset) {

        // For now, just log. The tests use embedded Kafka to push messages here.
        log.info("Received tx on topic={} offset={} -> {}", topic, offset, tx);
        // Don’t process yet; Task 2 only requires receiving & deserializing.
    }
}
