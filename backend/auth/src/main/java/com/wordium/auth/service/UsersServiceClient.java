package com.wordium.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.wordium.auth.dto.UserRequest;
import com.wordium.auth.dto.UserResponse;

@Service
public class UsersServiceClient {

    private final WebClient webClient;

    public UsersServiceClient() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8080")
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