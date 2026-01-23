package com.wordium.auth.service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.wordium.auth.client.UsersServiceClient;
import com.wordium.auth.dto.LoginRequest;
import com.wordium.auth.dto.SignUpRequest;
import com.wordium.auth.dto.UserResponse;
import com.wordium.auth.exceptions.BadRequestException;
import com.wordium.auth.model.AuthUser;
import com.wordium.auth.repo.AuthRepository;
import com.wordium.auth.security.JwtUtil;

@Service
public class AuthService {

    private final AuthRepository authRepository;
    private final UsersServiceClient usersServiceClient;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtUtil jwtUtil;
    private final CloudinaryService cloudinaryService;

    public AuthService(AuthRepository authRepository, UsersServiceClient usersServiceClient, JwtUtil jwtUtil,
            CloudinaryService cloudinaryService) {
        this.authRepository = authRepository;
        this.usersServiceClient = usersServiceClient;
        this.jwtUtil = jwtUtil;
        this.cloudinaryService = cloudinaryService;
    }

    public String registerUser(SignUpRequest req) {
        SignUpRequest signUpRequest = new SignUpRequest(req.email(), req.username(), null, req.bio(),
                req.avatar(), req.location(), null);

        UserResponse userResponse = usersServiceClient.createUser(signUpRequest);

        AuthUser authUser = new AuthUser();
        authUser.setUserId(userResponse.id());
        authUser.setPasswordHash(passwordEncoder.encode(req.password()));

        authRepository.save(authUser);

        // update avatar status if exist
        if (req.avatar() != null && !req.avatar().isBlank()) {
            if (req.avatarPublicId() == null || req.avatarPublicId().isBlank()) {
                throw new IllegalArgumentException("publicId must not be null or empty");
            }

            cloudinaryService.finalizeUpload(req.avatarPublicId());
        }
        String token = jwtUtil.generateToken(userResponse.id(), userResponse.role());

        return token;
    }

    public String validateUser(LoginRequest req) {

        UserResponse user = usersServiceClient.validateUser(req.email(), req.username());

        Optional<AuthUser> authUserOpt = authRepository.findByUserId(user.id());
        AuthUser authUser = authUserOpt.orElseThrow(() -> new BadRequestException("Invalid credentials"));

        if (!passwordEncoder.matches(req.password(), authUser.getPasswordHash())) {
            throw new BadRequestException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.id(), user.role());

        return token;
    }

}
