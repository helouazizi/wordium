package com.wordium.auth.dto;

import jakarta.validation.constraints.NotBlank;

public class RegisterRequest extends LoginRequest {

    @NotBlank(message = "Full name is required")
    private String fullName;

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getFullName() {
        return fullName;
    }
}
