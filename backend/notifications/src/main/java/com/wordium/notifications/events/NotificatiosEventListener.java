package com.wordium.notifications.events;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.wordium.notifications.dto.NotificationEvent;
import com.wordium.notifications.services.NotificationsService;

@Component
public class NotificatiosEventListener {

    private final NotificationsService notificationsService;

    public NotificatiosEventListener(NotificationsService notificationsService) {
        this.notificationsService = notificationsService;
    }

    @KafkaListener(
            topics = "notifications.events",
            groupId = "notifications-service"
    )
    public void handleEvent(NotificationEvent event) {
        notificationsService.createFromEvent(event);
    }
}
