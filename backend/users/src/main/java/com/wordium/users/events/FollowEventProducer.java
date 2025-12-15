package com.wordium.users.events;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.wordium.users.dto.FollowEvent;

@Component
public class FollowEventProducer {

    private static final String TOPIC = "user.follow.events";

    private final KafkaTemplate<String, FollowEvent> kafkaTemplate;

    public FollowEventProducer(KafkaTemplate<String, FollowEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendFollowEvent(FollowEvent event) {
        kafkaTemplate.send(
                TOPIC,
                new FollowEvent(
                        event.type(),
                        event.actorId(),
                        event.receiverId(),
                        event.referenceId())
        );
    }
}
