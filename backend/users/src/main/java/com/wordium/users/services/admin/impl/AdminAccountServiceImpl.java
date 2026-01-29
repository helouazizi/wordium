package com.wordium.users.services.admin.impl;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.wordium.users.dto.Role;
import com.wordium.users.dto.Stats;
import com.wordium.users.dto.notification.NotificationEvent;
import com.wordium.users.dto.users.UserProfile;
import com.wordium.users.events.FollowEventProducer;
import com.wordium.users.exceptions.NotFoundException;
import com.wordium.users.models.Users;
import com.wordium.users.repo.FollowersRepo;
import com.wordium.users.repo.UsersRepo;
import com.wordium.users.services.admin.AdminAccountService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AdminAccountServiceImpl implements AdminAccountService {

    private final UsersRepo usersRepo;
    private final FollowersRepo followersRepo;
    private final FollowEventProducer producer;

    public AdminAccountServiceImpl(UsersRepo usersRepo, FollowersRepo followersRepo, FollowEventProducer producer) {
        this.usersRepo = usersRepo;
        this.followersRepo = followersRepo;
        this.producer = producer;
    }

    public long getTotalUsers() {
        return usersRepo.count();
    }

    @Override
    public Page<UserProfile> getAllAccounts(
            Long viewerId,
            Pageable pageable) {
        return usersRepo.findAll(pageable)
                .map(u -> toUserProfile(u, viewerId));
    }

    @Override
    public Users getAccountById(Long id) {
        return usersRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
    }

    @Override
    public Users banAccount(Long id) {
        Users account = getAccountById(id);
        account.setBanned(true);
        account.setUpdatedAt(LocalDateTime.now());
        return usersRepo.save(account);
    }

    @Override
    public Users unbanAccount(Long id) {
        Users account = getAccountById(id);
        account.setBanned(false);
        account.setUpdatedAt(LocalDateTime.now());
        return usersRepo.save(account);
    }

    @Override
    public Users changeRole(Long id, Role newRole) {
        Users account = getAccountById(id);
        account.setRole(newRole);
        account.setUpdatedAt(LocalDateTime.now());
        return usersRepo.save(account);
    }

    @Override
    public void deleteAccount(Long id) {
        Users account = usersRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("User  not found"));

        usersRepo.delete(account);

        NotificationEvent deleteUserEvent = new NotificationEvent(
                "DELETE_USER",
                account.getId(),
                null,
                null);

        producer.sendFollowEvent(deleteUserEvent);

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
