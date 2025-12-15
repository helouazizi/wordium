package  com.wordium.notifications.services;

import org.springframework.stereotype.Service;

import com.wordium.notifications.repo.NotificationsRepo;



@Service
public class NotificationsService {

    private final NotificationsRepo repository;

    public NotificationsService(NotificationsRepo repository) {
        this.repository = repository;
    }

    // public Notification create(String title, String message, Long userId) {
    //     return repository.save(new Notification(title, message, userId));
    // }

    // public List<Notification> getUserNotifications(Long userId) {
    //     return repository.findByUserId(userId);
    // }

    // public List<Notification> getUnreadNotifications(Long userId) {
    //     return repository.findByUserIdAndReadFalse(userId);
    // }

    // public void markAsRead(Long id) {
    //     Notification notification = repository.findById(id)
    //             .orElseThrow(() -> new RuntimeException("Notification not found"));

    //     notification.setRead(true);
    //     repository.save(notification);
    // }
}