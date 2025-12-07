package com.wordium.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wordium.auth.dto.LoginRequest;
import com.wordium.auth.dto.LoginResponse;
import com.wordium.auth.dto.RegisterRequest;
import com.wordium.auth.dto.UserResponse;
import com.wordium.auth.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class AuthController {

    @Autowired
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody RegisterRequest req) {
        authService.registerUser(req);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        String token = authService.validateUser(req);
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @GetMapping("/by-email")
    public ResponseEntity<?> getFakeUser(@RequestParam String email) {
        return ResponseEntity.ok(new UserResponse(12301L, email));
    }

    @PostMapping("/fake-post")
    public ResponseEntity<?> fakePost(@RequestBody RegisterRequest req) {
        return ResponseEntity.ok(new UserResponse(12301L, req.getEmail()));
    }
}