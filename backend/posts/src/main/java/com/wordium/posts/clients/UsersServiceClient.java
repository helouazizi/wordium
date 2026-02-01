package com.wordium.posts.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.wordium.posts.dto.BatchUsersRequest;
import com.wordium.posts.dto.UserProfile;

@FeignClient(name = "users-service", url = "${services.api.url}")
public interface UsersServiceClient {

    @PostMapping("/api/v1/users/internal/batch")
    List<UserProfile> getUsersByIds(@RequestBody BatchUsersRequest request);

    @GetMapping("/api/v1/users/internal/{userId}/following")
    List<Long> getFollowingIds(@PathVariable("userId") Long userId);
}
