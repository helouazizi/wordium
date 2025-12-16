package com.wordium.wsgateway.notifications.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.wordium.wsgateway.notifications.model.Notification;

@Service
public class NotificationSocketService {

    private final SimpMessagingTemplate messaging;

    public NotificationSocketService(SimpMessagingTemplate messaging) {
        this.messaging = messaging;
    }

    public void push(Notification notification) {
        messaging.convertAndSendToUser(
                notification.getReceiverUserId().toString(),
                "/queue/notifications",
                // NotificationDTO.from(notification)
                notification
        );
    }
}
