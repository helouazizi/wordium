package com.wordium.users.service;

import org.springframework.stereotype.Service;

import com.wordium.users.dto.CreateUserRequest;
import com.wordium.users.dto.UserResponse;
import com.wordium.users.exceptions.ConflictException;
import com.wordium.users.exceptions.DatabaseException;
import com.wordium.users.exceptions.NotFoundException;
import com.wordium.users.model.User;
import com.wordium.users.repo.UserRepo;

@Service
public class UserService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public UserResponse createUser(CreateUserRequest req) {
        if (userRepo.findByEmail(req.email()).isPresent()) {
            throw new ConflictException("Email or username already taken");
        }
        User user = new User();
        user.setEmail(req.email());
        user.setUsername(req.username());
        userRepo.save(user);

        return new UserResponse(
                user.getId(),
                user.getEmail());

    }

    public UserResponse getUserByEmail(String email) {
        try {
            User user = userRepo.findByEmail(email).orElseThrow(() -> new NotFoundException("Email not found"));

            return new UserResponse(
                    user.getId(),
                    user.getEmail());

        } catch (Exception e) {
            throw new DatabaseException("Failed to create user", e);
        }

    }

}
