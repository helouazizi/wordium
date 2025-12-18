package com.wordium.wsgateway.common.dto;

import java.time.Instant;

public record NotificationResponse(
        Long id,
        String type,
        UserProfile actor,
        boolean read,
        Instant createdAt
        ) {

}
