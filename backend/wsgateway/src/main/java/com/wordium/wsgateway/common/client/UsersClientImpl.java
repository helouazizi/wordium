package com.wordium.wsgateway.common.client;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.wordium.wsgateway.common.dto.BatchUsersRequest;
import com.wordium.wsgateway.common.dto.UserProfile;

@Component
public class UsersClientImpl implements UsersClient {

    private final WebClient webClient;
    private final String serviceToken;

    public UsersClientImpl(
            @Value("${GATEWAY_API:http://localhost:8080}") String gatewayApi,
            @Value("${INTERNAL_SERVICE_SECRET}") String serviceToken) {

        this.webClient = WebClient.builder()
                .baseUrl(gatewayApi)
                .build();

        this.serviceToken = serviceToken;
    }

    @Override
    public List<UserProfile> getUsersByIds(Collection<Long> userIds) {

        if (userIds == null || userIds.isEmpty()) {
            return List.of();
        }
        BatchUsersRequest request = new BatchUsersRequest(userIds.stream().toList());

        return webClient.post()
                .uri("/api/v1/users/internal/batch")
                .header("Internal-Service-Token", serviceToken)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<UserProfile>>() {
                })
                .block();
    }

}
