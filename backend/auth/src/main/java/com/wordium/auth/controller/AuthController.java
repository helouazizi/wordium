package com.wordium.auth.controller;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wordium.auth.dto.AuthResponse;
import com.wordium.auth.dto.LoginRequest;
import com.wordium.auth.dto.SignUpRequest;
import com.wordium.auth.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

        private final AuthService authService;

        public AuthController(AuthService authService) {
                this.authService = authService;
        }

        @Operation(summary = "Register a new user", description = "Creates a new user account and returns a JWT token.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "User registered successfully", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Validation Errors", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
        })
        @PostMapping(value = "/signup", consumes = "multipart/form-data")
        public ResponseEntity<AuthResponse> signup(
                        @Valid @ModelAttribute SignUpRequest req) {
                String token = authService.registerUser(req);
                return ResponseEntity.ok(new AuthResponse(token));
        }

        @Operation(summary = "Authenticate a user", description = "Validates user credentials and returns a JWT token.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "User authenticated successfully", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid credentials", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
        })
        @PostMapping(value = "/login", consumes = "application/json")

        public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
                String token = authService.validateUser(req);
                return ResponseEntity.ok(new AuthResponse(token));
        }
}
