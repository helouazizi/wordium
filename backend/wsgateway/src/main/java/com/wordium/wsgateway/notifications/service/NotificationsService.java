package com.wordium.wsgateway.notifications.service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
        messagingTemplate.convertAndSend(destination, "New notification " + event.receiverId());

        return saved;
    }

    public List<Notification> getUserNotifications(Long userId) {
        List<Notification> notifications = repository.findByReceiverUserIdOrderByCreatedAtDesc(userId);
        Set<Long> actorIds = notifications.stream()
                .map(Notification::getActorUserId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        // now need to get users profile from users 
        // List<UserProfile> profiles = userClient.getUsersByIds(actorIds);

        // Map<Long, UserProfile> userProfileMap = profiles.stream()
        // .collect(Collectors.toMap(UserProfile::id, Function.identity()));
        // List<NotificationResponse> responses = notifications.stream()
        // .map(notification -> new NotificationResponse(
        //         notification.getId(),
        //         notification.getType(),
        //         userProfileMap.get(notification.getActorUserId()),
        //         notification.isRead(),
        //         notification.getCreatedAt()
        // ))
        // .toList();


        return notifications;
    }

    public void markAsRead(Long notificationId, Long userId) {
        int updated = repository.markAsRead(notificationId, userId);

        System.out.println("Updated rows: " + updated);
    }
}
