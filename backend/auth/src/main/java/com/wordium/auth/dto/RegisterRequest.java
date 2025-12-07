package com.wordium.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest extends LoginRequest {
    // @NotBlank(message = "Email is required")
    // @Email(message = "Email must be valid")
    // private String email;

    // @NotBlank(message = "Password is required")
    // @Size(min = 6, message = "Password must be at least 6 characters")
    // private String password;

    @NotBlank(message = "Full name is required")
    private String fullName;
}
