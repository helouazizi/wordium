package com.wordium.users.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.wordium.users.events.FollowEvent;

@Service
public class FollowEventProducer {

    private static final String TOPIC = "user.follow.events";

    private final KafkaTemplate<String, FollowEvent> kafkaTemplate;

    public FollowEventProducer(KafkaTemplate<String, FollowEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendFollowEvent(FollowEvent event) {
        kafkaTemplate.send(
                TOPIC,
                event.getFollowerId(), // key → keeps ordering per user
                event
        );
    }
}
