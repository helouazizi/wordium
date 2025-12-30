package com.wordium.users.dto.users;

public record UsersResponse(Long id, String role, String email, String username, String bio, String avatar,
        String location) {
}
