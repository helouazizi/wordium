package com.wordium.wsgateway.notifications.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class TestWsController {

    private final SimpMessagingTemplate messagingTemplate;

    public TestWsController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // Client will send a message to /app/test
    @MessageMapping("/test")
    public void testConnection(String payload) {
        // Just send back a simple confirmation
        messagingTemplate.convertAndSend("/topic/test", "WS connection successful! Payload received: " + payload);
    }
}
