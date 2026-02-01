package com.wordium.users.events;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.wordium.users.dto.notification.NotificationEvent;
import com.wordium.users.services.followers.FollowersService;

@Component
public class PostEventConsumer {
    private final FollowersService followersService;

    public PostEventConsumer(FollowersService followersService) {
        this.followersService = followersService;
    }

    @KafkaListener(topics = "postCreation.events", groupId = "users-service")
    public void consume(NotificationEvent event) {
        followersService.notifyFollowers(event);
        System.out.println("event recived seccefully " + event.type());
    }
}
