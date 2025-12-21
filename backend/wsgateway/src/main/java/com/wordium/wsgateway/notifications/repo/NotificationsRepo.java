package com.wordium.wsgateway.notifications.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.wordium.wsgateway.notifications.model.Notification;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;

public interface NotificationsRepo extends JpaRepository<Notification, Long> {

    List<Notification> findByReceiverUserIdOrderByCreatedAtDesc(Long receiverId);
    long countByReceiverUserIdAndIsReadFalse(Long receiverUserId);
    long countByReceiverUserId(Long receiverUserId);


    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.id = :id AND n.receiverUserId = :userId")
    int markAsRead(@Param("id") Long id, @Param("userId") Long userId);
}
