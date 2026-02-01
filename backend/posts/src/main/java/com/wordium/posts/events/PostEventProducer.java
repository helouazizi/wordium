package com.wordium.posts.events;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.wordium.posts.dto.NotificationEvent;

@Component
public class PostEventProducer {
    private static final String TOPIC = "postCreation.events";

    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    public PostEventProducer(KafkaTemplate<String, NotificationEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendPostEvent(NotificationEvent event) {
        kafkaTemplate.send(
                TOPIC,
                new NotificationEvent(
                        event.type(),
                        event.actorId(),
                        event.receiverId(),
                        event.referenceId()));
    }
}
