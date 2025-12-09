package com.wordium.auth.service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.wordium.auth.dto.AuthRequest;
import com.wordium.auth.dto.UserRequest;
import com.wordium.auth.dto.UserResponse;
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

    public void registerUser(AuthRequest req) {
        UserRequest userRequest = new UserRequest(req.email(), req.username(), req.displayName(), req.bio(),
                req.location(), req.avatarUrl());

        UserResponse userResponse = usersServiceClient.createUser(userRequest);

        AuthUser authUser = new AuthUser();
        authUser.setUserId(userResponse.id());
        authUser.setPasswordHash(passwordEncoder.encode(req.password()));

        authRepository.save(authUser);
    }

    public String validateUser(AuthRequest req) {

        UserResponse user = usersServiceClient.getByEmail(req);

        if (user == null) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        Optional<AuthUser> authUserOpt = authRepository.findByUserId(user.id());
        AuthUser authUser = authUserOpt.orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(req.password(), authUser.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.id(),user.role());

        return token;
    }

}
