package com.wordium.users.repo;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.wordium.users.models.Users;

@Repository
public interface UsersRepo extends JpaRepository<Users, Long>, JpaSpecificationExecutor<Users> {
    Optional<Users> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Users> findByUsername(String username);

    @Override
    Optional<Users> findById(Long id);

    List<Users> findAllByIdIn(Collection<Long> ids);
}
