package com.wordium.posts.dto;

public record UserProfile(Long id, String role, String email, String username, String bio, String avatar,
        String location) {

}
