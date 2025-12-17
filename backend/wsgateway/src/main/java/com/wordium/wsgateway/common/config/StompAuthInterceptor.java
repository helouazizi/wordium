package com.wordium.wsgateway.common.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import com.wordium.wsgateway.util.JwtUtil;
@Component
public class StompAuthInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;

    public StompAuthInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor =
            MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) return message;

        System.out.println("➡ STOMP COMMAND = " + accessor.getCommand());

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            String authHeader = accessor.getFirstNativeHeader("Authorization");

            System.out.println("➡ Authorization header = " + authHeader);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new IllegalArgumentException("Missing JWT in STOMP CONNECT");
            }

            String token = authHeader.substring(7);
            var userInfo = jwtUtil.extractUserInfo(token);

            accessor.getSessionAttributes().put("userId", userInfo.userId());

            System.out.println("🟢 STOMP authenticated user " + userInfo.userId());
        }

        return message;
    }
}
