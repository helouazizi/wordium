package com.wordium.users.dto.users;

import com.wordium.users.dto.Social;

import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(

        @Size(max = 50, message = "DisplayName must be less than 50 characters") String displayName,

        @Size(max = 500, message = "Bio must be less than 500 characters") String bio,

        String avatar,
        String cover,

        @Size(max = 500, message = "Location must be less than 500 characters") String location,

        Social social

) {
}
