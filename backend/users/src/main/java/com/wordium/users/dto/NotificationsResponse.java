package com.wordium.users.dto;

import java.time.Instant;
import java.util.List;

 record NotificationResponse(
        Long id,
        String type,
        UserProfile actor,
        boolean read,
        Instant createdAt
        ) {

}


public record NotificationsResponse(
        List<NotificationResponse> notifications,
        long unreadCount,
        long totalCount
) {}

record UserProfile(Long id, String username, String avatarUrl) {

}
