package com.wordium.auth.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.wordium.auth.model.AuthUser;
import com.wordium.auth.repo.AuthRepository;

import jakarta.transaction.Transactional;

@Component
public class AdminUserInitializer implements CommandLineRunner {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserInitializer(AuthRepository authRepository, PasswordEncoder passwordEncoder) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (authRepository.existsByUserId(1L)) {
            System.out.println("Admin already exists, skipping creation.");
            return;
        }

        AuthUser admin = new AuthUser();
        admin.setUserId(1L);
        admin.setPasswordHash(passwordEncoder.encode("admin1"));

        authRepository.save(admin);

        System.out.println("Admin user created");
    }
}