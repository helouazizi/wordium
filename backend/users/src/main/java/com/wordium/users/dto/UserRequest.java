package com.wordium.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
                @NotBlank(message = "Email is required") @Email(message = "Email must be valid") String email,

                @NotBlank(message = "Username is required") String username,
                @NotBlank(message = "Display name is required") @Size(min = 3, max = 50, message = "Display name must be between 3 and 50 characters") String displayName,

                @Size(max = 200, message = "Bio must be less than 200 characters") String bio,

                String avatarUrl,

                @Size(max = 50, message = "Location must be less than 50 characters") String location) {
}
