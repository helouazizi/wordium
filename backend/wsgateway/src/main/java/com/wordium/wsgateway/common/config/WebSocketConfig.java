package com.wordium.wsgateway.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.wordium.wsgateway.common.websocket.UserHandshakeHandler;



@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final WebSocketAuthInterceptor authInterceptor;
    private final UserHandshakeHandler handshakeHandler;

    public WebSocketConfig(
            WebSocketAuthInterceptor authInterceptor,
            UserHandshakeHandler handshakeHandler
    ) {
        this.authInterceptor = authInterceptor;
        this.handshakeHandler = handshakeHandler;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
            .addEndpoint("/ws")
            .addInterceptors(authInterceptor)
            .setHandshakeHandler(handshakeHandler)
            .setAllowedOriginPatterns("*")
            .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        // server → client
        registry.enableSimpleBroker("/queue", "/topic");

        // client → server
        registry.setApplicationDestinationPrefixes("/app");

        // user destinations
        registry.setUserDestinationPrefix("/user");
    }
}
