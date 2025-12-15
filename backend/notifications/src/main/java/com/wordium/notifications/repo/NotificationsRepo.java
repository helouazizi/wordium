package com.wordium.notifications.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wordium.notifications.models.Notification;

public interface NotificationsRepo extends JpaRepository<Notification, Long> {

    // List<Notification> findByUserId(Long userId);

    // List<Notification> findByUserIdAndReadFalse(Long userId);
}
