package com.wordium.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthRequest(
                @NotBlank(message = "Email is required") @Email(message = "Email must be valid") String email,
                @NotBlank(message = "Password is required") @Size(min = 6, max = 30, message = "password must be betwen 6 -> 30 caracter") String password,
                @NotBlank(message = "Username is required") @Size(min = 3, max = 30, message = "username must be betwen 3 -> 30 caracter") String username,

                @NotBlank(message = "Display name is required") @Size(min = 3, max = 50, message = "Display name must be between 3 and 50 characters") String displayName,

                @Size(max = 200, message = "Bio must be less than 200 characters") String bio,

                String avatarUrl,

                @Size(max = 50, message = "Location must be less than 50 characters") String location) {

}
