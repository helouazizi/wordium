package com.wordium.users.dto.users;

import java.time.LocalDateTime;

import com.wordium.users.dto.Role;
import com.wordium.users.dto.Social;
import com.wordium.users.dto.Stats;

public record UserProfile(
                Long id,
                String username,
                String displayName,
                String email,
                Role role,
                String avatar,
                String cover,
                String bio,
                String location,
                LocalDateTime createdAt,
                LocalDateTime updatedAt,
                LocalDateTime lastLoginAt,
                Boolean isVerified,
                Boolean isBanned,
                Boolean isFollowing, // optional for frontend
                Boolean followsMe, // optional for frontend
                Stats stats, // nested record for counts
                Social social // nested record for social links
) {


}
