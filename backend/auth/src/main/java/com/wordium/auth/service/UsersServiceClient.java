package com.wordium.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.wordium.auth.dto.CreateUserRequest;
import com.wordium.auth.dto.UserResponse;

@Service
public class UsersServiceClient {

    private final WebClient webClient;

    // @Value("${users.service.url}")
    public UsersServiceClient(String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public UserResponse checkEmail(CreateUserRequest req) {
        return webClient.post()
                .uri("/users/check_email")
                .bodyValue(req)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .block(); // synchronous
    }

    public UserResponse createUser(CreateUserRequest req) {
        return webClient.post()
                .uri("/users")
                .bodyValue(req)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .block(); // synchronous
    }
}