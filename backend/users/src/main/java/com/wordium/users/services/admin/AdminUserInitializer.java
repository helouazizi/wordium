package com.wordium.users.services.admin;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.wordium.users.dto.Role;
import com.wordium.users.models.Users;
import com.wordium.users.repo.UsersRepo;

import jakarta.transaction.Transactional;

@Component
public class AdminUserInitializer implements CommandLineRunner {

    private final UsersRepo usersRepo;

    public AdminUserInitializer(UsersRepo usersRepo) {
        this.usersRepo = usersRepo;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        String adminEmail = "admin1@admin1.com"; 
        if (usersRepo.existsByEmail(adminEmail)) {
            System.out.println("Admin already exists, skipping creation.");
            return;
        }

        Users admin = new Users();
        admin.setEmail(adminEmail);
        admin.setUsername("admin1");
        admin.setRole(Role.ADMIN);
        usersRepo.save(admin);

        System.out.println("Admin user created");
    }
}