package com.wordium.notifications.services;

import org.springframework.stereotype.Service;

import com.wordium.notifications.dto.NotificationEvent;
import com.wordium.notifications.models.Notification;
import com.wordium.notifications.repo.NotificationsRepo;

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

        return repository.save(notification);
    }

    

}
