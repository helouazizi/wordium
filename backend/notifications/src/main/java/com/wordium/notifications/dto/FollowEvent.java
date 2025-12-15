package com.wordium.notifications.dto;

public record FollowEvent(
        String type,
        Long actorId,
        Long receiverId,
        Long referenceId
        ) {

}
