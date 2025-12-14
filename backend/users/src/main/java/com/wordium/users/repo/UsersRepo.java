package com.wordium.users.repo;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wordium.users.model.Users;

@Repository
public interface UsersRepo extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);

    Optional<Users> findByUsername(String username);

    @Override
    Optional<Users> findById(Long id);
     List<Users> findAllByIdIn(Collection<Long> ids);
}
