package com.wordium.wsgateway.notifications.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wordium.wsgateway.notifications.model.Notification;
import com.wordium.wsgateway.notifications.service.NotificationsService;

@Controller
@RequestMapping("/notifications")
public class NotificationsController {

    private final NotificationsService service;

    public NotificationsController(NotificationsService service) {
        this.service = service;
    }

    @GetMapping
    public List<Notification> getMyNotifications(
            @RequestHeader("User-Id") Long userId
    ) {
        return null;
    }

    @GetMapping("/unread-count")
    public long unreadCount(
            @RequestHeader("User-Id") Long userId
    ) {
        return 0;
    }

    @PostMapping("/{id}/read")
    public void markAsRead(
            @PathVariable Long id,
            @RequestHeader("User-Id") Long userId
    ) {
        service.markAsRead(id, userId);
    }
}
