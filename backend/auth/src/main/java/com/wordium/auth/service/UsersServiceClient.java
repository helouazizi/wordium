package com.wordium.auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.wordium.auth.dto.AuthRequest;
import com.wordium.auth.dto.SignUpRequest;
import com.wordium.auth.dto.SuccessResponse;
import com.wordium.auth.dto.UserResponse;
import com.wordium.auth.util.ServiceCaller;

import reactor.core.publisher.Mono;

@Service
public class UsersServiceClient {

    private final WebClient webClient;
    private final String serviceToken;
    private final ServiceCaller serviceCaller;

    public UsersServiceClient(
            @Value("${GATEWAY_API:http://localhost:8080}") String gatewayApi,
            @Value("${INTERNAL_SERVICE_SECRET}") String serviceToken, ServiceCaller serviceCaller) {
        this.webClient = WebClient.builder()
                .baseUrl(gatewayApi)
                .build();

        this.serviceToken = serviceToken;
        this.serviceCaller = serviceCaller;
    }

    public UserResponse createUser(SignUpRequest req) {
        Mono<SuccessResponse<UserResponse>> responseMono = webClient.post()
                .uri("/api/v1/users/create")
                .header("Internal-Service-Token", serviceToken)
                .bodyValue(req)
                .retrieve()
                .bodyToMono(UserResponse.class);
        return serviceCaller.callService(responseMono);

    }

    public UserResponse getByEmail(AuthRequest req) {
        Mono<UserResponse> responseMono = webClient.post()
                .uri("/api/v1/users/by-email")
                .header("Internal-Service-Token", serviceToken)
                .bodyValue(req)
                .retrieve()
                .bodyToMono(UserResponse.class);
        return serviceCaller.callService(responseMono);
    }
}
