package com.wordium.wsgateway.notifications.dto;

public record NotificationEvent(
        String type,
        Long actorId,
        Long receiverId,
        Long referenceId
        ) {

}
