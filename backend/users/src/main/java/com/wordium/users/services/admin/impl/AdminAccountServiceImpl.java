package com.wordium.users.services.admin.impl;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.wordium.users.dto.Role;
import com.wordium.users.models.Users;
import com.wordium.users.repo.UsersRepo;
import com.wordium.users.services.admin.AdminAccountService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AdminAccountServiceImpl implements AdminAccountService {

    private final UsersRepo usersRepo;

    public AdminAccountServiceImpl(UsersRepo usersRepo) {
        this.usersRepo = usersRepo;
    }

    @Override
    public Page<Users>  getAllAccounts(Pageable pageable) {
        Page<Users> users = usersRepo.findAll(pageable);
        return users;
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
        Users account = getAccountById(id);
        usersRepo.delete(account);
    }
}
