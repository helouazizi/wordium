package com.wordium.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @Email(message = "Email must be valid") String email,

        String username,

        @NotBlank(message = "Password is required") String password) {

}
