package com.wordium.wsgateway.notifications.events;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.wordium.wsgateway.notifications.dto.NotificationEvent;
import com.wordium.wsgateway.notifications.service.NotificationsService;

@Component
public class DeletedUserEventConsumer {
    private final NotificationsService notificationsService;

    public DeletedUserEventConsumer(NotificationsService notificationsService) {
        this.notificationsService = notificationsService;
    }

    @KafkaListener(topics = "users.events", groupId = "ws-gateway")
    public void consume(NotificationEvent event) {
        notificationsService.createFromEvent(event);
        System.out.println("event recived seccefully " + event.type());
    }
}
