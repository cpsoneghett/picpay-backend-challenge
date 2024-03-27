package com.cpsoneghett.api.notification;

import com.cpsoneghett.api.transaction.TransactionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class NotificationConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationConsumer.class);
    private final RestClient restClient;

    public NotificationConsumer(RestClient.Builder restClient) {
        this.restClient = restClient
                .baseUrl("http://localhost:8990/notification")
                .build();
    }

    @KafkaListener(topics = "transaction-notification", groupId = "picpay-backend-challenge")
    public void receiveNotification(TransactionDTO transaction) {

        LOGGER.info("notifying transaction... {} ", transaction);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TransactionDTO> request = new HttpEntity<>(transaction, headers);

        var response = restClient.post()
                .body(request)
                .retrieve()
                .toEntity(TransactionDTO.class);

        if (response.getStatusCode().isError() || response.getBody() == null)
            throw new NotificationException("Error sending notification!");

        LOGGER.info("notification has been sent... {} ", response.getBody());
    }
}
