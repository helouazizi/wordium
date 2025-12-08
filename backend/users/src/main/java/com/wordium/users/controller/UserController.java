package com.wordium.users.controller;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wordium.users.dto.CreateUserRequest;
import com.wordium.users.dto.UserResponse;
import com.wordium.users.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest req) {
        UserResponse createdUser = userService.createUser(req);
        return ResponseEntity.status(HttpStatus.SC_CREATED).body(createdUser);
    }

    @PostMapping("/by-email")
    public ResponseEntity<UserResponse> getUserByEmail(@Valid @RequestBody CreateUserRequest req) {
        UserResponse user = userService.getUserByEmail(req.email());
        return ResponseEntity.ok(user);
    }

}
