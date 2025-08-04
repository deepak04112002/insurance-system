package com.insurance.system.service;

import com.insurance.system.entity.User;
import com.insurance.system.repository.UserRepository;
import com.insurance.system.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .password("encodedPassword")
                .role(User.Role.AGENT)
                .build();
    }

    @Test
    void testRegisterUser() {
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User user = User.builder()
                .name("Test User")
                .email("test@example.com")
                .password("password")
                .role(User.Role.AGENT)
                .build();

        User result = authService.register(user);

        assertNotNull(result);
        assertEquals("Test User", result.getName());
        verify(passwordEncoder).encode("password");
        verify(userRepository).save(user);
    }

    @Test
    void testLoginSuccess() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("test@example.com", "AGENT")).thenReturn("jwt-token");

        String token = authService.login("test@example.com", "password");

        assertEquals("jwt-token", token);
    }

    @Test
    void testLoginUserNotFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                authService.login("test@example.com", "password"));
    }

    @Test
    void testLoginInvalidPassword() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongpassword", "encodedPassword")).thenReturn(false);

        assertThrows(RuntimeException.class, () ->
                authService.login("test@example.com", "wrongpassword"));
    }

}
