package com.wordium.auth.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wordium.auth.model.AuthUser;
import com.wordium.auth.model.User;

@Repository
public interface AuthRepository extends JpaRepository<AuthUser, Long> {
    Optional<User> findByEmail(String email);
}
