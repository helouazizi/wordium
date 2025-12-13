package com.wordium.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
                @NotBlank(message = "Email is required") @Email(message = "Email must be valid") String email,

                @NotBlank(message = "Username is required") @Size(max = 30, min = 6, message = "Username must be 6 -> 30 characters") String username,

                @Size(max = 300, message = "Bio must be less than 300 characters") String bio,

                @Size(max = 500, message = "avatarUrl must be less than 500 characters") String avatarUrl,

                @Size(max = 300, message = "Location must be less than 30 characters") String location) {

}
