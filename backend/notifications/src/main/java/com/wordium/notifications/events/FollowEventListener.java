package com.wordium.notifications.events;

import java.util.Map;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.wordium.notifications.models.Notification;
import com.wordium.notifications.repo.NotificationsRepo;

@Component
public class FollowEventListener {

    private final NotificationsRepo notificationRepository;

    public FollowEventListener(NotificationsRepo notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @KafkaListener(
            topics = "user.follow.events",
            groupId = "notifications-service"
    )
    public void handleFollowEvent(Map<String, Object> event) {
        String type = (String) event.get("type");
        Long actorId = Long.valueOf(event.get("actorId").toString());
        Long receiverId = Long.valueOf(event.get("receiverId").toString());
        Long referenceId = Long.valueOf(event.get("referenceId").toString());

        Notification notification = new Notification(
                receiverId,
                actorId,
                type,
                referenceId
        );

        notificationRepository.save(notification);
    }
}
