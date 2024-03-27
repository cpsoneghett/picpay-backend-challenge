package com.cpsoneghett.api.notification;

import com.cpsoneghett.api.transaction.TransactionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationProducer notificationProducer;

    public NotificationService(NotificationProducer notificationProducer) {
        this.notificationProducer = notificationProducer;
    }

    public void notify(TransactionDTO transaction) {
        LOGGER.info("notifying transaction {}...", transaction);

        notificationProducer.sendNotification(transaction);
    }
}
