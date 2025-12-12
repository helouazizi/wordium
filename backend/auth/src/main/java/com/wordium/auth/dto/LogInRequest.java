package com.wordium.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LogInRequest(
        @NotBlank(message = "Email is required") @Email(message = "Email must be valid") String email,
        @NotBlank(message = "Password is required") @Size(min = 6, max = 30, message = "password must be betwen 6 -> 30 caracter") String password,
        @NotBlank(message = "Username is required") @Size(min = 3, max = 30, message = "username must be betwen 3 -> 30 caracter") String username) {

}
