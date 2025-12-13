package com.wordium.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
        @NotBlank(message = "Email is required") @Email(message = "Email must be valid") String email,

        @NotBlank(message = "Username is required") String username,

        @Size(max = 300, message = "Bio must be less than 300 characters") String bio,

        String avatarUrl,

        @Size(max = 300, message = "Location must be less than 30 characters") String location) {

}
