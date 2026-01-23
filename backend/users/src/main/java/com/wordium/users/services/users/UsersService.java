package com.wordium.users.services.users;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.wordium.users.dto.Social;
import com.wordium.users.dto.auth.SignUpRequest;
import com.wordium.users.dto.auth.SignUpResponse;
import com.wordium.users.dto.users.BatchUsersRequest;
import com.wordium.users.dto.users.UpdateProfileRequest;
import com.wordium.users.dto.users.UserProfile;
import com.wordium.users.exceptions.BadRequestException;
import com.wordium.users.exceptions.ConflictException;
import com.wordium.users.exceptions.NotFoundException;
import com.wordium.users.models.Users;
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
        user.setAvatar(req.avatar());
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

    public UserProfile getUserProfile(Long userId) {
        Users user = usersRepo.findById(userId).orElseThrow(() -> new NotFoundException("User Not Found"));

        return new UserProfile(
                user.getId(),
                user.getUsername(), user.getDisplayName(), user.getEmail(), user.getRole(),
                user.getAvatar(), user.getCover(), user.getBio(), user.getLocation(), user.getCreatedAt(),
                user.getUpdatedAt(), user.getLastLoginAt(), user.isVerified(), user.isBanned(), null, null, null,
                user.getSocial());

    }

    public UserProfile getProfile(Long userId) {
        Users user = usersRepo.findById(userId).orElseThrow(() -> new NotFoundException("User Not Found"));

        return new UserProfile(
                user.getId(),
                user.getUsername(), user.getDisplayName(), user.getEmail(), user.getRole(),
                user.getAvatar(), user.getCover(), user.getBio(), user.getLocation(), user.getCreatedAt(),
                user.getUpdatedAt(), user.getLastLoginAt(), user.isVerified(), user.isBanned(), null, null, null,
                user.getSocial());

    }

    public List<UserProfile> getUsers(BatchUsersRequest req) {
        Set<Long> ids = new HashSet<>(req.usersIds());
        List<Users> users = usersRepo.findAllByIdIn(ids);

        if (ids.isEmpty()) {
            return List.of();
        }
        return users.stream()
                .map(user -> new UserProfile(
                        user.getId(),
                        user.getUsername(), user.getDisplayName(), user.getEmail(), user.getRole(),
                        user.getAvatar(), user.getCover(), user.getBio(), user.getLocation(), user.getCreatedAt(),
                        user.getUpdatedAt(), user.getLastLoginAt(), user.isVerified(), user.isBanned(), null, null,
                        null, user.getSocial()))
                .toList();
    }

    public UserProfile updateUserProfile(Long userId, UpdateProfileRequest req) {
        Users user = usersRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (req.displayName() != null) {
            user.setDisplayName(req.displayName());
        }
        if (req.bio() != null) {
            user.setBio(req.bio());
        }
        if (req.location() != null) {
            user.setLocation(req.location());
        }
        if (req.avatar() != null) {
            user.setAvatar(req.avatar());
        }

        if (req.cover() != null) {
            user.setCover(req.cover());
        }

        if (req.social() != null) {
            Social current = user.getSocial() ;

            if (req.social().getWebsite() != null)
                current.setWebsite(req.social().getWebsite());
            if (req.social().getTwitter() != null)
                current.setTwitter(req.social().getTwitter());
            if (req.social().getGithub() != null)
                current.setGithub(req.social().getGithub());
            if (req.social().getLinkedin() != null)
                current.setLinkedin(req.social().getLinkedin());

            user.setSocial(current);
        }

        try {
            usersRepo.save(user);
        } catch (Exception e) {
            throw new BadRequestException("Failed to update profile");
        }

        return new UserProfile(
                user.getId(),
                user.getUsername(), user.getDisplayName(), user.getEmail(), user.getRole(),
                user.getAvatar(), user.getCover(), user.getBio(), user.getLocation(), user.getCreatedAt(),
                user.getUpdatedAt(), user.getLastLoginAt(), user.isVerified(), user.isBanned(), null, null, null,
                user.getSocial());
    }

}
