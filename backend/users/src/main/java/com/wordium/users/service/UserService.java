package com.wordium.users.service;

import org.springframework.stereotype.Service;

import com.wordium.users.dto.UserRequest;
import com.wordium.users.dto.UserResponse;
import com.wordium.users.exceptions.BadRequestException;
import com.wordium.users.exceptions.ConflictException;
import com.wordium.users.model.User;
import com.wordium.users.repo.UserRepo;

@Service
public class UserService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public UserResponse createUser(UserRequest req) {
        if (userRepo.findByEmail(req.email()).isPresent()) {
            throw new ConflictException("Email or username already taken");
        }
        User user = new User();
        user.setEmail(req.email());
        user.setUsername(req.username());
        user.setBio(req.bio());
        user.setDisplayName(req.displayName());
        user.setLocation(req.location());
        userRepo.save(user);

        return new UserResponse(
                user.getId(),
                user.getRole(), null);

    }

    public UserResponse getUserByEmail(String email) {
        try {
            User user = userRepo.findByEmail(email).orElseThrow(() -> new BadRequestException("Invalid Credentials"));

            return new UserResponse(
                    user.getId(),
                    user.getRole(), user.getEmail());

        } catch (Exception e) {
            throw new BadRequestException("Invalid Credentials");
        }

    }

}
