package com.wordium.wsgateway.common.dto;

import java.util.List;

public record NotificationsResponse(
        List<NotificationResponse> notifications,
        long unreadCount,
        long totalCount
) {}
