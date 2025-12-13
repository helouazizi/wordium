package com.wordium.users.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wordium.users.dto.UsersResponse;
import com.wordium.users.exceptions.BadRequestException;
import com.wordium.users.exceptions.NotFoundException;
import com.wordium.users.model.Followers;
import com.wordium.users.model.Users;
import com.wordium.users.repo.FollowersRepo;
import com.wordium.users.repo.UserRepo;

@Service
public class FollowersService {

    private final FollowersRepo followersRepo;
    private final UserRepo userRepo;

    public FollowersService(FollowersRepo followersRepo, UserRepo userRepo) {
        this.followersRepo = followersRepo;
        this.userRepo = userRepo;
    }

    @Transactional
    public void followUser(Long userId, Long targetUserId) {
        if (userId.equals(targetUserId)) {
            throw new BadRequestException("You cannot follow yourself");
        }

        Users follower = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        Users followed = userRepo.findById(targetUserId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (followersRepo.existsByFollowerIdAndFollowedId(userId, targetUserId)) {
            throw new BadRequestException("Already following this user");
        }

        Followers follow = new Followers(follower.getId(), followed.getId());
        followersRepo.save(follow);
    }

    @Transactional
    public void unfollowUser(Long userId, Long targetUserId) {
        Followers follow = followersRepo.findByFollowerIdAndFollowedId(userId, targetUserId)
                .orElseThrow(() -> new BadRequestException("You are Not following this user"));
        followersRepo.delete(follow);
    }

    public List<UsersResponse> getFollowers(Long userId) {
        userRepo.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));

        List<Followers> followers = followersRepo.findByFollowedId(userId);

        return followers.stream()
                .map(f -> userRepo.findById(f.getFollowerId())
                        .map(u -> new UsersResponse(u.getId(), u.getRole(), u.getEmail(),
                                u.getUsername(), u.getDisplayName(), u.getBio(), u.getAvatar(), u.getLocation()))
                        .orElse(null))
                .filter(r -> r != null)
                .collect(Collectors.toList());
    }
}
