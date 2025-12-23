package com.wordium.users.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.wordium.users.dto.NotificationsResponse;

@FeignClient(name = "wsgateway", url = "http://localhost:8080/api/v1/wsgateway")
public interface WsgatewayServiceClient {

    @GetMapping("/internal/notifications")
    NotificationsResponse getAllNotif(
            @RequestHeader("User-Id") String userId
    );
}
