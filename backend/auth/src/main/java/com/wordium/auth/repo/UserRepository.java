package com.wordium.auth.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wordium.auth.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
