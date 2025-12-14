package com.wordium.users.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.wordium.users.dto.BatchUsersRequest;
import com.wordium.users.dto.SignUpRequest;
import com.wordium.users.dto.SignUpResponse;
import com.wordium.users.dto.UpdateProfileRequest;
import com.wordium.users.dto.UsersResponse;
import com.wordium.users.exceptions.BadRequestException;
import com.wordium.users.exceptions.ConflictException;
import com.wordium.users.exceptions.NotFoundException;
import com.wordium.users.model.Users;
import com.wordium.users.repo.UsersRepo;

@Service
public class UsersService {

    private final UsersRepo usersRepo;

    public UsersService(UsersRepo usersRepo) {
        this.usersRepo = usersRepo;
    }

    public SignUpResponse createUser(SignUpRequest req) {
        if (usersRepo.findByEmail(req.email()).isPresent()) {
            throw new ConflictException("Email or username already taken");
        }
        Users user = new Users();
        user.setEmail(req.email());
        user.setUsername(req.username());
        user.setBio(req.bio());
        user.setLocation(req.location());
        usersRepo.save(user);

        return new SignUpResponse(
                user.getId(),
                user.getRole());

    }

    public SignUpResponse findByEmailOrUsername(String email, String username) {
        Users user;

        if (email != null && !email.isBlank()) {
            user = usersRepo.findByEmail(email)
                    .orElseThrow(() -> new BadRequestException("Invalid credentials"));
        } else if (username != null && !username.isBlank()) {
            user = usersRepo.findByUsername(username)
                    .orElseThrow(() -> new BadRequestException("Invalid credentials"));
        } else {
            throw new BadRequestException("Email or username must be provided");
        }

        return new SignUpResponse(
                user.getId(),
                user.getRole());
    }

    public UsersResponse getUserProfile(Long userId) {
        Users user = usersRepo.findById(userId).orElseThrow(() -> new NotFoundException("User Not Found"));

        return new UsersResponse(
                user.getId(),
                user.getRole(), user.getEmail(), user.getUsername(), user.getBio(),
                user.getAvatarUrl(), user.getLocation());

    }

    public List<UsersResponse> getUsers(BatchUsersRequest req) {
        Set<Long> ids = new HashSet<>(req.usersIds());
        List<Users> users = usersRepo.findAllByIdIn(ids);

        if (ids.isEmpty()) {
            return List.of();
        }
        return users.stream()
                .map(user -> new UsersResponse(
                user.getId(), // id
                user.getRole(), // role
                user.getEmail(), // email
                user.getUsername(), // username
                user.getBio(), // bio
                user.getAvatarUrl(), // avatar
                user.getLocation() // location
        )).toList();
    }

    public UsersResponse updateUserProfile(Long userId, UpdateProfileRequest req) {
        Users user = usersRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (req.email() != null && !req.email().equals(user.getEmail())) {
            if (usersRepo.findByEmail(req.email()).isPresent()) {
                throw new ConflictException("Email is already in use");
            }
            user.setEmail(req.email());
        }

        if (req.username() != null && !req.username().equals(user.getUsername())) {
            if (usersRepo.findByUsername(req.username()).isPresent()) {
                throw new ConflictException("Username is already in use");
            }
            user.setUsername(req.username());
        }

        if (req.bio() != null) {
            user.setBio(req.bio());
        }
        if (req.location() != null) {
            user.setLocation(req.location());
        }
        if (req.avatarUrl() != null) {
            user.setAvatarUrl(req.avatarUrl());
        }

        try {
            usersRepo.save(user);
        } catch (Exception e) {
            throw new BadRequestException("Failed to update profile: " + e.getMessage());
        }

        return new UsersResponse(
                user.getId(),
                user.getRole(),
                user.getEmail(),
                user.getUsername(),
                user.getBio(),
                user.getAvatarUrl(),
                user.getLocation());
    }

}
