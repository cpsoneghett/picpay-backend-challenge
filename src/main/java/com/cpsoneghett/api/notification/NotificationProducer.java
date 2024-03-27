package com.cpsoneghett.api.notification;

import com.cpsoneghett.api.transaction.TransactionDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class NotificationProducer {

    private final KafkaTemplate<String, TransactionDTO> kafkaTemplate;

    public NotificationProducer(KafkaTemplate<String, TransactionDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendNotification(TransactionDTO transaction) {

        kafkaTemplate.send("transaction-notification", transaction);
    }
}
