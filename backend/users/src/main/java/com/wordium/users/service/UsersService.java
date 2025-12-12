package com.wordium.users.service;

import org.springframework.stereotype.Service;

import com.wordium.users.dto.SignUpRequest;
import com.wordium.users.dto.UsersResponse;
import com.wordium.users.exceptions.BadRequestException;
import com.wordium.users.exceptions.ConflictException;
import com.wordium.users.exceptions.NotFoundException;
import com.wordium.users.model.Users;
import com.wordium.users.repo.UserRepo;

@Service
public class UsersService {

    private final UserRepo userRepo;

    public UsersService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public UsersResponse createUser(SignUpRequest req) {
        if (userRepo.findByEmail(req.email()).isPresent()) {
            throw new ConflictException("Email or username already taken");
        }
        Users user = new Users();
        user.setEmail(req.email());
        user.setUsername(req.username());
        user.setBio(req.bio());
        user.setLocation(req.location());
        userRepo.save(user);

        return new UsersResponse(
                user.getId(),
                user.getRole(), null, null, null, null, null, null);

    }

    public UsersResponse getUserByEmail(String email) {
        Users user = userRepo.findByEmail(email).orElseThrow(() -> new BadRequestException("Invalid Credentials"));

        return new UsersResponse(
                user.getId(),
                user.getRole(), user.getEmail(), null, null, null, null, null);

    }

    public UsersResponse getUserProfile(Long userId) {
        Users user = userRepo.findById(userId).orElseThrow(() -> new NotFoundException("User Not Found"));

        return new UsersResponse(
                user.getId(),
                user.getRole(), user.getEmail(), user.getUsername(), user.getDisplayName(), user.getBio(),
                user.getAvatar(), user.getLocation());
    }

    public UsersResponse updateUserProfile(Long userId, SignUpRequest req) {
        if (!userRepo.findById(userId).isPresent()) {
            throw new BadRequestException("User Not Found");
        }
        Users user = new Users();
        user.setEmail(req.email());
        user.setUsername(req.username());
        user.setBio(req.bio());
        user.setLocation(req.location());
        userRepo.save(user);
        return new UsersResponse(
                user.getId(),
                user.getRole(), user.getEmail(), user.getUsername(), user.getDisplayName(), user.getBio(),
                user.getAvatar(), user.getLocation());

    }

}
