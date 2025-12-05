package com.wordium.auth.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.wordium.auth.model.User;
import com.wordium.auth.repo.UserRepository;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User validateUser(String email, String rawPassword) throws IllegalArgumentException {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty())
            throw new IllegalArgumentException("User not found");

        User user = userOpt.get();

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return user;
    }

    // // Get all users
    // public List<User> getAllUsers() {
    // return userRepository.findAll();
    // }

    // // Get user by ID
    // public User getUserById(Long id) {
    // return userRepository.findById(id).orElseThrow(() -> new
    // IllegalArgumentException("user not found"));
    // }

    // Create new user
    // public User createUser(User user) {
    // // validation
    // if (user.getEmail() == null || user.getEmail().isBlank()) {
    // throw new IllegalArgumentException("Email cannot be empty");
    // }

    // // if (user.getPassword() == null || user.getPassword().isBlank()) {
    // // throw new IllegalArgumentException("Password cannot be empty");
    // // }

    // // check duplicates
    // // if (userRepository.existsByEmail(user.getEmail())) {
    // // throw new IllegalStateException("Email already exists");
    // // }

    // return userRepository.save(user);
    // }

    // Update user
    // public User updateUser(Long id, User updatedUser) {
    // User existingUser = getUserById(id);
    // existingUser.setName(updatedUser.getName());
    // existingUser.setEmail(updatedUser.getEmail());
    // return userRepository.save(existingUser);
    // }

    // // Delete user
    // public void deleteUser(Long id) {
    // User existingUser = getUserById(id);
    // userRepository.delete(existingUser);
    // }
}
