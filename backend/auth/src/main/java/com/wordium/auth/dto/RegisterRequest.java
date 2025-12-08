package com.wordium.auth.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;



public record RegisterRequest(LoginRequest login,
        @NotBlank(message = "Username is required") @Size(min = 3, max = 30, message = "username must be betwen 3 -> 30 caracter") String username) {}
