package com.wordium.users.services.followers;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.wordium.users.dto.Stats;
import com.wordium.users.dto.notification.NotificationEvent;
import com.wordium.users.dto.users.UserProfile;
import com.wordium.users.events.FollowEventProducer;
import com.wordium.users.exceptions.BadRequestException;
import com.wordium.users.exceptions.NotFoundException;
import com.wordium.users.models.Followers;
import com.wordium.users.models.Users;
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
                producer.sendFollowEvent(
                                new NotificationEvent("FOLLOW", follow.getFollowerId(), follow.getFollowedId(), null));
        }

        @Transactional
        public void unfollowUser(Long userId, Long targetUserId) {
                Followers follow = followersRepo.findByFollowerIdAndFollowedId(userId, targetUserId)
                                .orElseThrow(() -> new BadRequestException("You are Not following this user"));
                followersRepo.delete(follow);

                // notify using kafka
                producer.sendFollowEvent(
                                new NotificationEvent("UNFOLLOW", follow.getFollowerId(), follow.getFollowedId(),
                                                null));
        }

        public Page<UserProfile> getFollowers(
                        Long viewerId,
                        Long userId,
                        Pageable pageable) {
                usersRepo.findById(userId)
                                .orElseThrow(() -> new NotFoundException("User Not Found"));

                return followersRepo.findByFollowedId(userId, pageable)
                                .map(f -> {
                                        Users u = usersRepo.findById(f.getFollowerId())
                                                        .orElseThrow(() -> new NotFoundException(
                                                                        "Follower user not found"));
                                        return toUserProfile(u, viewerId);
                                });
        }

        public Page<UserProfile> getFollowing(
                        Long viewerId,
                        Long userId,
                        Pageable pageable) {
                usersRepo.findById(userId)
                                .orElseThrow(() -> new NotFoundException("User Not Found"));

                return followersRepo.findByFollowerId(userId, pageable)
                                .map(f -> {
                                        Users u = usersRepo.findById(f.getFollowedId())
                                                        .orElseThrow(() -> new NotFoundException(
                                                                        "Followed user not found"));
                                        return toUserProfile(u, viewerId);
                                });
        }

        private UserProfile toUserProfile(Users u, Long viewerId) {

                Boolean isFollowing = viewerId != null &&
                                followersRepo.existsByFollowerIdAndFollowedId(viewerId, u.getId());

                Boolean followsMe = viewerId != null &&
                                followersRepo.existsByFollowerIdAndFollowedId(u.getId(), viewerId);

                Stats stats = new Stats(
                                followersRepo.countByFollowedId(u.getId()),
                                followersRepo.countByFollowerId(u.getId()), null, null);

                return new UserProfile(
                                u.getId(),
                                u.getUsername(),
                                u.getDisplayName(),
                                u.getEmail(),
                                u.getRole(),
                                u.getAvatar(),
                                u.getCover(),
                                u.getBio(),
                                u.getLocation(),
                                u.getCreatedAt(),
                                u.getUpdatedAt(),
                                u.getLastLoginAt(),
                                u.isVerified(),
                                u.isBanned(),
                                isFollowing,
                                followsMe,
                                stats,
                                u.getSocial());
        }

}
