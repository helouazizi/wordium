package com.wordium.auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.wordium.auth.dto.AuthRequest;
import com.wordium.auth.dto.UserRequest;
import com.wordium.auth.dto.UserResponse;
import com.wordium.auth.exceptions.ExternalServiceException;

@Service
public class UsersServiceClient {

    private final WebClient webClient;
    private final String serviceToken;

    public UsersServiceClient(
            @Value("${GATEWAY_API:http://localhost:8080}") String gatewayApi,
            @Value("${INTERNAL_SERVICE_SECRET}") String serviceToken) {
        this.webClient = WebClient.builder()
                .baseUrl(gatewayApi)
                .build();

        this.serviceToken = serviceToken;
    }

    public UserResponse createUser(UserRequest req) {
        try {
            return webClient.post()
                    .uri("/api/v1/users/")
                    .header("Internal-Service-Token", serviceToken)
                    .bodyValue(req)
                    .retrieve()
                    .bodyToMono(UserResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new ExternalServiceException(
                    e.getStatusCode().value(),
                    "User Service Error",
                    e.getResponseBodyAsString(),
                    null);
        }
    }

    public UserResponse getByEmail(AuthRequest req) {
        try {
            return webClient.post()
                    .uri("/api/v1/users/by-email")
                    .header("Internal-Service-Token", serviceToken)
                    .bodyValue(req)
                    .retrieve()
                    .bodyToMono(UserResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new ExternalServiceException(
                    e.getStatusCode().value(),
                    "User Service Error",
                    e.getResponseBodyAsString(),
                    null);
        }
    }
}
