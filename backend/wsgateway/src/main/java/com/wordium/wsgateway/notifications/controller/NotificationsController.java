package com.wordium.wsgateway.notifications.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wordium.wsgateway.common.dto.NotificationResponse;
import com.wordium.wsgateway.notifications.service.NotificationsService;

@RestController
@RequestMapping("/wsgateway")
public class NotificationsController {

    private final NotificationsService service;

    public NotificationsController(NotificationsService service) {
        this.service = service;
    }

    @GetMapping("/notifications")
    public List<NotificationResponse> getMyNotifications(
            @RequestHeader("User-Id") Long userId
    ) {
        return service.getUserNotifications(userId);
    }

    @GetMapping("/notifications/unread-count")
    public long unreadCount(
            @RequestHeader("User-Id") Long userId
    ) {
        return 0;
    }

    @PostMapping("/notifications/{id}/read")
    public void markAsRead(
            @PathVariable Long id,
            @RequestHeader("User-Id") Long userId
    ) {
        service.markAsRead(id, userId);
    }
}
