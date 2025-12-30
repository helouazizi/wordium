package com.wordium.users.events;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.wordium.users.dto.notification.NotificationEvent;

@Component
public class FollowEventProducer {

    private static final String TOPIC = "notifications.events";

    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    public FollowEventProducer(KafkaTemplate<String, NotificationEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendFollowEvent(NotificationEvent event) {
        kafkaTemplate.send(
                TOPIC,
                new NotificationEvent(
                        event.type(),
                        event.actorId(),
                        event.receiverId(),
                        event.referenceId())
        );
    }
}
