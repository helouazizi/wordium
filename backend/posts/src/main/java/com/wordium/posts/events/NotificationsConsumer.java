package com.wordium.posts.events;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.wordium.posts.dto.NotificationEvent;
import com.wordium.posts.services.impl.PostServiceImpl;

@Component
public class NotificationsConsumer {

    private final PostServiceImpl postService;

    public NotificationsConsumer(PostServiceImpl postService) {
        this.postService = postService;
    }

    @KafkaListener(topics = "users.events", groupId = "posts-service")
    public void consume(NotificationEvent event) {
        postService.handleUserDeleted(event.actorId());
        System.out.println("event recived seccefully " + event.type());
    }
}
