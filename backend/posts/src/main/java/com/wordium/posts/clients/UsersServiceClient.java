package com.wordium.posts.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.wordium.posts.dto.BatchUsersRequest;
import com.wordium.posts.dto.UserProfile;

@FeignClient(name = "users-service", url = "${services.api.url}")
public interface UsersServiceClient {

    @PostMapping("/api/v1/users/internal/batch")
    List<UserProfile> getUsersByIds(@RequestBody BatchUsersRequest request);
}
