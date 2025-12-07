package com.wordium.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Username is required")
    @Email(message = "Username must be valid")
    private String username;

    @NotBlank(message = "Password is required")
    // @Size(min = 6, message = "Password must be at least 6 characters")
    @Size(min = 6, max = 30, message = "password must be betwen 6 -> 30 caracter")

    private String password;
}
