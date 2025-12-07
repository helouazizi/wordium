package com.wordium.auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.wordium.auth.dto.UserRequest;
import com.wordium.auth.dto.UserResponse;

@Service
public class UsersServiceClient {

    private final WebClient webClient;

    public UsersServiceClient(@Value("${GATEWAY_API:http://localhost:8080}") String gatewayApi) {
        this.webClient = WebClient.builder()
                .baseUrl(gatewayApi)
                .build();
    }

    public UserResponse createUser(UserRequest req) {
        return webClient.post()
                .uri("/auth/users/fake-post")
                .bodyValue(req)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .block(); // synchronous
    }

    public UserResponse getByEmail(String email) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/auth/users/by-email")
                        .queryParam("email", email)
                        .build())
                .retrieve()
                .bodyToMono(UserResponse.class)
                .block();
    }
}
