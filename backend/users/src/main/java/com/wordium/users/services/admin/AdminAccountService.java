package com.wordium.users.services.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wordium.users.dto.Role;
import com.wordium.users.dto.users.UserProfile;
import com.wordium.users.models.Users;

public interface AdminAccountService {
    Page<UserProfile> getAllAccounts(Long viewerId, Pageable pageable);

    Users getAccountById(Long id);

    Users banAccount(Long id);

    Users unbanAccount(Long id);

    Users changeRole(Long id, Role newRole);

    void deleteAccount(Long id);

    Page<UserProfile> getSearchUsers(
            Long viewerId,
            Pageable pageable);
}