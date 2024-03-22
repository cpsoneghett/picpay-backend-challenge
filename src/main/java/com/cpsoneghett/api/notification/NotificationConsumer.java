package com.cpsoneghett.api.notification;

import com.cpsoneghett.api.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class NotificationConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationConsumer.class);
    private RestClient restClient;

    public NotificationConsumer(RestClient.Builder restClient) {
        this.restClient = restClient
                .baseUrl("https://run.mocky.io/v3/54dc2cf1-3add-45b5-b5a9-6bf7e7f1f4a6")
                .build();
    }

    @KafkaListener(topics = "transaction-notification", groupId = "picpay-backend-challenge")
    public void receiveNotification(Transaction transaction) {

        LOGGER.info("notifying transaction... {} ", transaction);

        var response = restClient.get()
                .retrieve()
                .toEntity(Notification.class);

        if(response.getStatusCode().isError() || !response.getBody().isMessage())
            throw new NotificationException("Error sending notification!");

        LOGGER.info("notification has been sent... {} ", response.getBody());
    }
}
