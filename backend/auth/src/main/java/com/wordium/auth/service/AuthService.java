package com.wordium.auth.service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.wordium.auth.dto.UserRequest;
import com.wordium.auth.dto.LoginRequest;
import com.wordium.auth.dto.RegisterRequest;
import com.wordium.auth.dto.UserResponse;
import com.wordium.auth.exceptions.ConflictException;
import com.wordium.auth.model.AuthUser;
import com.wordium.auth.repo.AuthRepository;
import com.wordium.auth.security.JwtUtil;

@Service
public class AuthService {

    private final AuthRepository authRepository;
    private final UsersServiceClient usersServiceClient;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtUtil jwtUtil;

    public AuthService(AuthRepository authRepository, UsersServiceClient usersServiceClient, JwtUtil jwtUtil) {
        this.authRepository = authRepository;
        this.usersServiceClient = usersServiceClient;
        this.jwtUtil = jwtUtil;
    }

    public void registerUser(RegisterRequest req) {
        UserRequest userRequest = new UserRequest(req.getEmail(), req.getFullName());

        UserResponse existingUser = usersServiceClient.getByEmail(req.getEmail());
        if (existingUser != null) {
            throw new ConflictException("Email already exists");
        }

        UserResponse userResponse = usersServiceClient.createUser(userRequest);

        AuthUser authUser = new AuthUser();
        authUser.setUserId(userResponse.id());
        authUser.setPasswordHash(passwordEncoder.encode(req.getPassword()));

        authRepository.save(authUser);
    }

    public String validateUser(LoginRequest req) {

        UserResponse user = usersServiceClient.getByEmail(req.getEmail());

        if (user == null) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        Optional<AuthUser> authUserOpt = authRepository.findByUserId(user.id());
        AuthUser authUser = authUserOpt.orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(req.getPassword(), authUser.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.email());

        return token;
    }

}
