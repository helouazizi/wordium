package com.wordium.users.dto;

public record UserResponse(Long id, String role,String email,String username , String dispalyName , String bio , String avatar ,String location) {
}
