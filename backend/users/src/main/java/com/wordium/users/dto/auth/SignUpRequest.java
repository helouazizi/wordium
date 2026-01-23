package com.wordium.users.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
        @NotBlank(message = "Email is required") @Email(message = "Email must be valid") String email,

        @NotBlank(message = "Username is required") @Size(max = 30, min = 6, message = "Username must be 6 -> 30 characters") String username,

        @Size(max = 500, message = "Bio must be less than 500 characters") String bio,

        String avatar,
        @Size(max = 500, message = "Location must be less than 500 characters") String location

) {

}
