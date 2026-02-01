package com.wordium.wsgateway.notifications.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.wordium.wsgateway.common.client.UsersClientImpl;
import com.wordium.wsgateway.common.dto.NotificationResponse;
import com.wordium.wsgateway.common.dto.NotificationsResponse;
import com.wordium.wsgateway.common.dto.UserProfile;
import com.wordium.wsgateway.common.exceptions.NotFoundException;
import com.wordium.wsgateway.notifications.dto.NotificationEvent;
import com.wordium.wsgateway.notifications.model.Notification;
import com.wordium.wsgateway.notifications.repo.NotificationsRepo;

import jakarta.transaction.Transactional;

@Service
public class NotificationsService {

    private final NotificationsRepo repository;

    private final UsersClientImpl usersClientImpl;

    public NotificationsService(NotificationsRepo repository, UsersClientImpl usersClientImpl) {
        this.repository = repository;
        this.usersClientImpl = usersClientImpl;
    }

    @Transactional
    public Notification createFromEvent(NotificationEvent event) {

        return switch (event.type()) {

            case "FOLLOW", "UNFOLLOW", "POST_CREATED" ->
                createNotification(event);

            case "USER_DELETED" -> {
                deleteUserNotifications(event.actorId());
                yield null;
            }

            default -> throw new IllegalArgumentException(
                    "Unsupported notification event type: " + event.type());
        };
    }

    private Notification createNotification(NotificationEvent event) {

        Notification notification = new Notification(
                event.receiverId(),
                event.actorId(),
                event.type(),
                event.referenceId());

        try {
            Notification saved = repository.save(notification);
            System.out.println("notification created for " + event.receiverId());
            return saved;
        } catch (DataIntegrityViolationException e) {
            return null;
        }
    }

    @Transactional
    private void deleteUserNotifications(Long userId) {

        repository.deleteByActorUserId(userId);
        repository.deleteByReceiverUserId(userId);

        System.out.println("Deleted notifications for user " + userId);
    }

    public NotificationsResponse getUserNotifications(Long userId) {
        List<Notification> notifications = repository.findByReceiverUserIdOrderByCreatedAtDesc(userId);
        Set<Long> actorIds = notifications.stream()
                .map(Notification::getActorUserId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        List<UserProfile> profiles = usersClientImpl.getUsersByIds(actorIds);

        Map<Long, UserProfile> userProfileMap = profiles.stream()
                .collect(Collectors.toMap(UserProfile::id, Function.identity()));

        List<NotificationResponse> responses = notifications.stream()
                .map(notification -> new NotificationResponse(
                        notification.getId(),
                        notification.getType(),
                        userProfileMap.get(notification.getActorUserId()),
                        notification.isRead(),
                        notification.getCreatedAt()))
                .toList();
        long unread = repository.countByReceiverUserIdAndIsReadFalse(userId);
        long total = repository.countByReceiverUserId(userId);

        // save to cach i f neded

        return new NotificationsResponse(responses, unread, total);
    }

    public void markAsRead(Long notificationId, Long userId) {
        int updated = repository.markAsRead(notificationId, userId);
        if (updated == 0) {
            throw new NotFoundException("Notification with ID " + notificationId + " not found");
        }
    }

    public Long unreadCount(Long userId) {
        Long unread = repository.countByReceiverUserIdAndIsReadFalse(userId);

        return unread;
    }
}
