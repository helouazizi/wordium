package com.wordium.auth.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.wordium.auth.dto.AuthRequest;
import com.wordium.auth.dto.UserRequest;
import com.wordium.auth.dto.UserResponse;
import com.wordium.auth.exceptions.BadRequestException;
import com.wordium.auth.model.AuthUser;
import com.wordium.auth.repo.AuthRepository;
import com.wordium.auth.security.JwtUtil;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthRepository authRepository;

    @Mock
    private UsersServiceClient usersServiceClient;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    // -----------------------------------------
    // REGISTER TESTS
    // -----------------------------------------

    @Test
    void registerUser_shouldCreateUserAndReturnToken() {
        AuthRequest req = new AuthRequest(
                "test@mail.com", "password", "test", "test",
                "bio", "avatar", "test_location");

        UserResponse userResponse = new UserResponse(1L, "USER");

        when(usersServiceClient.createUser(any(UserRequest.class)))
                .thenReturn(userResponse);

        when(jwtUtil.generateToken(1L, "USER"))
                .thenReturn("token");

        String token = authService.registerUser(req);

        assertEquals("token", token);
        System.out.println(token);

        verify(authRepository, times(1)).save(any(AuthUser.class));

        verify(usersServiceClient, times(1)).createUser(any(UserRequest.class));
    }

    // -----------------------------------------
    // LOGIN TESTS
    // -----------------------------------------

    @Test
    void validateUser_shouldReturnToken_whenCredentialsAreValid() {

        AuthRequest req = new AuthRequest(
                "test@mail.com", "password", null, null, null, null, null);

        UserResponse user = new UserResponse(
                1L, "USER");

        AuthUser authUser = new AuthUser();
        authUser.setUserId(1L);
        authUser.setPasswordHash(new BCryptPasswordEncoder().encode("password"));

        when(usersServiceClient.getByEmail(req))
                .thenReturn(user);

        when(authRepository.findByUserId(1L))
                .thenReturn(Optional.of(authUser));

        when(jwtUtil.generateToken(1L, "USER"))
                .thenReturn("token");

        String token = authService.validateUser(req);

        assertEquals("token", token);
    }

    @Test
    void validateUser_shouldThrow_whenUserNotFound() {
        AuthRequest req = new AuthRequest("notfound@mail.com", "password", null,
                null, null, null, null);

        UserResponse user = new UserResponse(
                1L, "USER");

        when(usersServiceClient.getByEmail(req))
                .thenReturn(user);

        when(authRepository.findByUserId(1L))
                .thenReturn(Optional.empty());

        assertThrows(BadRequestException.class,
                () -> authService.validateUser(req));
    }

    @Test
    void validateUser_shouldThrow_whenPasswordInvalid() {
        AuthRequest req = new AuthRequest("test@mail.com", "whrongPass", null,
                null, null, null, null);

        UserResponse user = new UserResponse(
                1L, "USER");

        AuthUser authUser = new AuthUser();
        authUser.setUserId(1L);
        authUser.setPasswordHash(new BCryptPasswordEncoder().encode("correctpass"));

        when(usersServiceClient.getByEmail(req))
                .thenReturn(user);

        when(authRepository.findByUserId(1L))
                .thenReturn(Optional.of(authUser));

        assertThrows(BadRequestException.class,
                () -> authService.validateUser(req));
    }

}
