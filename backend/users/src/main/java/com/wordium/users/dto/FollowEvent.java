package com.wordium.users.dto;
public record FollowEvent(
        String type,
        Long actorId,
        Long receiverId,
        Long referenceId
        ) {

}