package com.wordium.wsgateway.notifications.service;

import org.springframework.stereotype.Service;

import com.wordium.wsgateway.notifications.dto.NotificationEvent;
import com.wordium.wsgateway.notifications.model.Notification;
import com.wordium.wsgateway.notifications.repo.NotificationsRepo;

@Service
public class NotificationsService {

    private final NotificationsRepo repository;

    public NotificationsService(NotificationsRepo repository) {
        this.repository = repository;

    }

    public Notification createFromEvent(NotificationEvent event) {

        Notification notification = new Notification(
                event.receiverId(),
                event.actorId(),
                event.type(),
                event.referenceId()
        );

        Notification saved = repository.save(notification);

        return saved;
    }

    public void markAsRead(Long notificationId, Long userId) {
        int updated = repository.markAsRead(notificationId, userId);

        System.out.println("Updated rows: " + updated);
    }
}
