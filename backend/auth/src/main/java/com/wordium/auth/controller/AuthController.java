package com.wordium.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wordium.auth.dto.AuthRequest;
import com.wordium.auth.dto.LoginResponse;
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
    public ResponseEntity<?> signup(@Valid @RequestBody AuthRequest req) {
        authService.registerUser(req);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        String token = authService.validateUser(req);
        return ResponseEntity.ok(new LoginResponse(token));
    }
}