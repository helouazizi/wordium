package com.wordium.wsgateway.notifications.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.wordium.wsgateway.notifications.dto.NotificationEvent;
import com.wordium.wsgateway.notifications.model.Notification;
import com.wordium.wsgateway.notifications.repo.NotificationsRepo;

@Service
public class NotificationsService {

    private final NotificationsRepo repository;

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationsService(NotificationsRepo repository, SimpMessagingTemplate messagingTemplate) {
        this.repository = repository;
        this.messagingTemplate = messagingTemplate;
    }

    public Notification createFromEvent(NotificationEvent event) {

        Notification notification = new Notification(
                event.receiverId(),
                event.actorId(),
                event.type(),
                event.referenceId()
        );

        Notification saved = repository.save(notification);
        // we need to push to front now  how to use ws here 
        // Push notification to user over WebSocket
        String destination = "/topic/test"; // temporary for testing
        messagingTemplate.convertAndSend(destination, "New notification for user " + event.receiverId());

        return saved;
    }

    public void markAsRead(Long notificationId, Long userId) {
        int updated = repository.markAsRead(notificationId, userId);

        System.out.println("Updated rows: " + updated);
    }
}
