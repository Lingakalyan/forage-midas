package com.jpmc.midascore.messaging;

import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.service.TransactionService;  // explicit import
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TransactionListener {

    private static final Logger log = LoggerFactory.getLogger(TransactionListener.class);
    private final TransactionService txService;

    public TransactionListener(TransactionService txService) {
        this.txService = txService;
    }

    @KafkaListener(
            topics = "${general.kafka-topic}",
            groupId = "midas-core",
            containerFactory = "transactionKafkaListenerContainerFactory"
    )
    public void onTransaction(Transaction tx) {
        txService.processIncoming(tx);   // <-- must match the service method name
        log.info("Processed incoming transaction: {}", tx);
    }
}
