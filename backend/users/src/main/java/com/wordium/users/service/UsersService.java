package com.wordium.users.service;

import org.springframework.stereotype.Service;

import com.wordium.users.dto.SignUpRequest;
import com.wordium.users.dto.SignUpResponse;
import com.wordium.users.dto.UsersResponse;
import com.wordium.users.exceptions.BadRequestException;
import com.wordium.users.exceptions.ConflictException;
import com.wordium.users.exceptions.NotFoundException;
import com.wordium.users.model.Users;
import com.wordium.users.repo.UsersRepo;

@Service
public class UsersService {

    private final UsersRepo userRepo;

    public UsersService(UsersRepo userRepo) {
        this.userRepo = userRepo;
    }

    public SignUpResponse createUser(SignUpRequest req) {
        if (userRepo.findByEmail(req.email()).isPresent()) {
            throw new ConflictException("Email or username already taken");
        }
        Users user = new Users();
        user.setEmail(req.email());
        user.setUsername(req.username());
        user.setBio(req.bio());
        user.setLocation(req.location());
        userRepo.save(user);

        return new SignUpResponse(
                user.getId(),
                user.getRole());

    }

    public SignUpResponse findByEmailOrUsername(String email, String username) {
        Users user;
        System.out.println(email+"emailllllllllllllllllll");
         System.out.println(username+"userssssssssssssssssssss");

        if (email != null && !email.isBlank()) {
            user = userRepo.findByEmail(email)
                    .orElseThrow(() -> new BadRequestException("Invalid credentials"));
        } else if (username != null && !username.isBlank()) {
            user = userRepo.findByUsername(username)
                    .orElseThrow(() -> new BadRequestException("Invalid credentials"));
        } else {
            throw new BadRequestException("Email or username must be provided");
        }

        return new SignUpResponse(
                user.getId(),
                user.getRole());
    }
    

    public UsersResponse getUserProfile(Long userId) {
        Users user = userRepo.findById(userId).orElseThrow(() -> new NotFoundException("User Not Found"));

        return new UsersResponse(
                user.getId(),
                user.getRole(), user.getEmail(), user.getUsername(), user.getBio(),
                user.getAvatar(), user.getLocation());

    }

}
