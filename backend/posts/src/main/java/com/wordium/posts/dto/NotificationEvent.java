package com.wordium.posts.dto;

public record NotificationEvent(
        String type,
        Long actorId,
        Long receiverId,
        Long referenceId
        ) {

}
