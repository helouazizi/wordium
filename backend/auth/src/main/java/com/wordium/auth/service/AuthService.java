package com.wordium.auth.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
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
    private final Cloudinary cloudinary;

    public AuthService(AuthRepository authRepository, UsersServiceClient usersServiceClient, JwtUtil jwtUtil,
            Cloudinary cloudinary) {
        this.authRepository = authRepository;
        this.usersServiceClient = usersServiceClient;
        this.jwtUtil = jwtUtil;
        this.cloudinary = cloudinary;
    }

    public String registerUser(SignUpRequest req) {
        String avatarUrl = uploadFile(req.avatar());
        SignUpRequest signUpRequest = new SignUpRequest(req.email(), req.username(), null, req.bio(),
                null, avatarUrl, req.location());

        UserResponse userResponse = usersServiceClient.createUser(signUpRequest);

        AuthUser authUser = new AuthUser();
        authUser.setUserId(userResponse.id());
        authUser.setPasswordHash(passwordEncoder.encode(req.password()));

        authRepository.save(authUser);
        String token = jwtUtil.generateToken(userResponse.id(), userResponse.role());

        return token;
    }

    private boolean isValidContentType(String contentType) {
        return contentType != null && (contentType.startsWith("image/"));
    }

    private String uploadFile(MultipartFile avatar) {
        if (avatar == null) {
            return null;
        }

        if (!isValidContentType(avatar.getContentType())) {
            throw new BadRequestException("Unsupported file type");
        }

        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    avatar.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "auto",
                            "folder", "avatars"));

            Object secureUrl = uploadResult.get("secure_url");
            if (secureUrl == null) {
                throw new BadRequestException("Upload failed: no URL returned");
            }

            return secureUrl.toString();

        } catch (Exception e) {

            e.printStackTrace();
            throw new BadRequestException("Failed to upload media file");
        }
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
