package com.wordium.auth.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.wordium.auth.dto.CreateUserRequest;
import com.wordium.auth.dto.RegisterRequest;
import com.wordium.auth.dto.UserResponse;
import com.wordium.auth.model.AuthUser;
import com.wordium.auth.model.User;
import com.wordium.auth.repo.AuthRepository;

@Service
public class AuthService {

    @Autowired
    private final AuthRepository authRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void registerUser(RegisterRequest req) {
        UsersServiceClient usersServiceClient = new UsersServiceClient("http://localhost:8080");
        CreateUserRequest userRequest = new CreateUserRequest(req.getEmail(), req.getFullName());

        // checkemail existance or username if needed
        UserResponse userResponse1 = usersServiceClient.checkEmail(userRequest);

        // create user safty
        UserResponse userResponse2 = usersServiceClient.createUser(userRequest);

        AuthUser authUser = new AuthUser();
        authUser.setUserId(userResponse2.id());
        authUser.setPassword(passwordEncoder.encode(req.getPassword()));

        authRepository.save(authUser);
    }

    public User validateUser(String email, String rawPassword) throws IllegalArgumentException {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty())
            throw new IllegalArgumentException("User not found");

        User user = userOpt.get();

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return user;
    }
}
