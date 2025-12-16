package com.wordium.notifications.dto;

public record NotificationEvent(
        String type,
        Long actorId,
        Long receiverId,
        Long referenceId
        ) {

}
