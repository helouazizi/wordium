package com.wordium.users.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.wordium.users.dto.NotificationEvent;
import com.wordium.users.dto.UsersResponse;
import com.wordium.users.events.FollowEventProducer;
import com.wordium.users.exceptions.BadRequestException;
import com.wordium.users.exceptions.NotFoundException;
import com.wordium.users.model.Followers;
import com.wordium.users.model.Users;
import com.wordium.users.repo.FollowersRepo;
import com.wordium.users.repo.UsersRepo;

import jakarta.transaction.Transactional;

@Service
public class FollowersService {

    private final FollowersRepo followersRepo;
    private final UsersRepo usersRepo;
    private final FollowEventProducer producer;

    public FollowersService(FollowersRepo followersRepo, UsersRepo usersRepo, FollowEventProducer producer) {
        this.followersRepo = followersRepo;
        this.usersRepo = usersRepo;
        this.producer = producer;
    }

    @Transactional
    public void followUser(Long userId, Long targetUserId) {
        if (userId.equals(targetUserId)) {
            throw new BadRequestException("You cannot follow yourself");
        }

        Users follower = usersRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        Users followed = usersRepo.findById(targetUserId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (followersRepo.existsByFollowerIdAndFollowedId(userId, targetUserId)) {
            throw new BadRequestException("Already following this user");
        }

        Followers follow = new Followers(follower.getId(), followed.getId());
        followersRepo.save(follow);

        // notify using kafka 
        producer.sendFollowEvent(new NotificationEvent( "FOLLOW",follow.getFollowerId(), follow.getFollowedId(),null));
    }

    @Transactional
    public void unfollowUser(Long userId, Long targetUserId) {
        Followers follow = followersRepo.findByFollowerIdAndFollowedId(userId, targetUserId)
                .orElseThrow(() -> new BadRequestException("You are Not following this user"));
        followersRepo.delete(follow);

        // notify using kafka 
        producer.sendFollowEvent(new NotificationEvent("UNFOLLOW",follow.getFollowerId(), follow.getFollowedId(),null));
    }

    public List<UsersResponse> getFollowers(Long userId) {
        usersRepo.findById(userId).orElseThrow(() -> new NotFoundException("User Not Found"));

        List<Followers> followers = followersRepo.findByFollowedId(userId);

        return followers.stream()
                .map(f -> usersRepo.findById(f.getFollowerId())
                .map(u -> new UsersResponse(u.getId(), u.getRole(), u.getEmail(),
                u.getUsername(), u.getBio(), u.getAvatarUrl(),
                u.getLocation()))
                .orElse(null))
                .filter(r -> r != null)
                .collect(Collectors.toList());
    }
}
