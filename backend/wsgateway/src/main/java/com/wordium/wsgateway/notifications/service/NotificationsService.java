package com.wordium.wsgateway.notifications.service;

import org.springframework.stereotype.Service;

import com.wordium.wsgateway.notifications.dto.NotificationEvent;
import com.wordium.wsgateway.notifications.model.Notification;
import com.wordium.wsgateway.notifications.repo.NotificationsRepo;

@Service
public class NotificationsService {

    private final NotificationsRepo repository;
    private final NotificationCacheService cacheService;
    private final NotificationSocketService socketService;

    public NotificationsService(NotificationsRepo repository, NotificationCacheService cacheService, NotificationSocketService socketService) {
        this.repository = repository;
        this.cacheService = cacheService;
        this.socketService = socketService;
    }

    public Notification createFromEvent(NotificationEvent event) {

        Notification notification = new Notification(
                event.receiverId(),
                event.actorId(),
                event.type(),
                event.referenceId()
        );

        Notification saved = repository.save(notification);
        cacheService.cache(saved);
        // socketService.push(saved);
        return saved;
    }

//     public long getUnreadCount(Long userId) {
//     return cacheService.getUnreadCount(userId);
// }
    // public List<Notification> getLatest(Long userId) {
    //     return cacheService.getLatest(userId)
    //             .orElseGet(() -> repository.findLatest(userId));
    // }
    public void markAsRead(Long notificationId, Long userId) {
        int updated = repository.markAsRead(notificationId, userId);

        cacheService.decrementUnread(userId);
        System.out.println("Updated rows: " + updated);
    }
}
