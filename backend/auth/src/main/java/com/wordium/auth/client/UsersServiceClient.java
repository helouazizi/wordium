package com.wordium.auth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.wordium.auth.dto.SignUpRequest;
import com.wordium.auth.dto.UserResponse;



@FeignClient(name = "users-service", url = "${services.api.url}/api/v1/users/internal")
public interface UsersServiceClient {

    @PostMapping("/create")
    UserResponse createUser(@RequestBody SignUpRequest request);

    @GetMapping("/lookup")
    UserResponse validateUser(@RequestParam String email,@RequestParam String username); // email or username 

}
