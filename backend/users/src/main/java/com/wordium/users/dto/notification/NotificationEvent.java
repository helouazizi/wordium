package com.wordium.users.dto.notification;
public record NotificationEvent(
        String type,
        Long actorId,
        Long receiverId,
        Long referenceId
        ) {

}