package com.wordium.users.services.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wordium.users.dto.Role;
import com.wordium.users.models.Users;

public interface AdminAccountService {
    Page<Users> getAllAccounts(Pageable pageable);
    Users getAccountById(Long id);
    Users banAccount(Long id);
    Users unbanAccount(Long id);
    Users changeRole(Long id, Role newRole);
    void deleteAccount(Long id);
}