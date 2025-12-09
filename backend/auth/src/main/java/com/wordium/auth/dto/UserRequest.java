package com.wordium.auth.dto;

public record UserRequest(String email, String username, String displayName , String bio ,String location,String avatarUrl) {
}
