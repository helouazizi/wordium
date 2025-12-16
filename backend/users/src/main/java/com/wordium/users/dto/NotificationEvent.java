package com.wordium.users.dto;
public record NotificationEvent(
        String type,
        Long actorId,
        Long receiverId,
        Long referenceId
        ) {

}