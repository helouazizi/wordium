package com.wordium.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wordium.auth.model.User;
import com.wordium.auth.security.JwtUtil;
import com.wordium.auth.service.UserService;

@RestController
@RequestMapping("/users")
public class AuthController {

    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        userService.registerUser(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) throws IllegalArgumentException {
        User validatedUser = userService.validateUser(user.getEmail(), user.getPassword());
        String token = jwtUtil.generateToken(validatedUser.getEmail());
        return ResponseEntity.ok(new JwtResponse(token));
    }

    static class JwtResponse {
        private final String token;

        public JwtResponse(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }
    }

    // // Get all users
    // @GetMapping
    // public List<User> getAllUsers() {
    // return userService.getAllUsers();
    // }

    // // get user by id
    // @GetMapping("/{id}")
    // public User getUserbyId(@PathVariable Long id) {
    // return userService.getUserById(id);
    // }

    // // Create a new user
    // @PostMapping
    // public ResponseEntity<?> createUser(@RequestBody User user) {
    // User saved = userService.createUser(user);
    // return ResponseEntity.ok(saved);
    // }

    // // get user by id
    // @PutMapping("/{id}")
    // public User updateUser(@PathVariable Long id, @RequestBody User user) {
    // return userService.updateUser(id, user);
    // }

    // @DeleteMapping("/{id}")
    // public void deleteUser(@PathVariable Long id) {
    // userService.deleteUser(id);
    // }
}