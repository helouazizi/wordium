package com.wordium.users.dto.users;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
                @Email(message = "Email must be valid") String email,

                @Size(max = 30, min = 6, message = "Username must be 6 -> 30 characters") String username,

                @Size(max = 500, message = "Bio must be less than 500 characters") String bio,
                MultipartFile avatar,

                String avatarUrl,

                @Size(max = 500, message = "Location must be less than 500 characters") String location) {

}
