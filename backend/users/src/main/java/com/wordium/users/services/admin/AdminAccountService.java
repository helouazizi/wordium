package com.wordium.users.services.admin;

import java.util.List;

import com.wordium.users.models.Users;

public interface AdminAccountService {
    List<Users> getAllAccounts();
    Users getAccountById(Long id);
    Users banAccount(Long id);
    Users unbanAccount(Long id);
    Users changeRole(Long id, String newRole);
    void deleteAccount(Long id);
}