package com.wordium.users.services.admin.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

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
    public List<Users> getAllAccounts() {
        // Specification<Users> spec = (root, query, cb) -> {
        // List<Predicate> predicates = new ArrayList<>();
        // if (banned != null) {
        // predicates.add(cb.equal(root.get("banned"), banned));
        // }
        // if (active != null) {
        // predicates.add(cb.equal(root.get("active"), active));
        // }
        // return cb.and(predicates.toArray(new Predicate[0]));
        // };

        return usersRepo.findAll();
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
    public Users changeRole(Long id, String newRole) {
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
