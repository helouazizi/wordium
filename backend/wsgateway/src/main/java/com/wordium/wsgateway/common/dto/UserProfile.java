package com.wordium.wsgateway.common.dto;

import java.time.LocalDateTime;

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
                ) 
                {

}
