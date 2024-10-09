package org.acme.demo.controller;
import org.acme.demo.entity.AppUser;
import org.acme.demo.entity.Role;
import org.acme.demo.entity.RoleName;
import org.acme.demo.security.JwtUtil;
import org.acme.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController authController;


    // Test for successful traditional login
    @Test
    public void testLoginUser_SuccessfulTraditionalLogin() {
        // Arrange
        AppUser mockUser = new AppUser();
        mockUser.setUsername("testuser");
        mockUser.setPassword("encodedPassword");

        Role mockRole = new Role();
        mockRole.setName(RoleName.ROLE_USER);

        Set<Role> mockRoles = Set.of(mockRole);
        mockUser.setRoles(mockRoles);

        when(userService.findUserByUsername("testuser")).thenReturn(mockUser);
        when(passwordEncoder.matches("password", mockUser.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(mockUser.getUsername(), mockUser.getRoles(), 1000)).thenReturn("mockJwtToken");

        // Act
        ResponseEntity<String> response = authController.loginUser(createUser("testuser", "password", null, null));

        // Assert
        assertEquals(200, response.getStatusCodeValue());  // Expect HTTP 200 OK
        assertEquals("mockJwtToken", response.getBody());  // Expect JWT token as the response
    }

    // Test for invalid username or password
    @Test
    public void testLoginUser_InvalidPassword() {
        // Arrange
        AppUser mockUser = new AppUser();
        mockUser.setUsername("testuser");
        mockUser.setPassword("encodedPassword");

        when(userService.findUserByUsername("testuser")).thenReturn(mockUser);
        when(passwordEncoder.matches("wrongpassword", mockUser.getPassword())).thenReturn(false);

        // Act
        ResponseEntity<String> response = authController.loginUser(createUser("testuser", "wrongpassword", null, null));

        // Assert
        assertEquals(401, response.getStatusCodeValue());  // Expect HTTP 401 Unauthorized
        assertEquals("Invalid username or password", response.getBody());
    }

    // Test for user not found
    @Test
    public void testLoginUser_UserNotFound() {
        // Arrange
        when(userService.findUserByUsername("nonexistentuser")).thenReturn(null);

        // Act
        ResponseEntity<String> response = authController.loginUser(createUser("nonexistentuser", "password", null, null));

        // Assert
        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Invalid username or password", response.getBody());
    }

    // Test for successful OAuth login
    @Test
    public void testLoginUser_SuccessfulOAuthLogin() {
        // Arrange
        AppUser mockUser = new AppUser();
        mockUser.setProvider("google");
        mockUser.setProviderId("google123");
        mockUser.setUsername("oauthuser");

        when(userService.findUserByProviderAndProviderId("google", "google123")).thenReturn(mockUser);
        when(jwtUtil.generateToken(mockUser.getUsername(), mockUser.getRoles(), 1000)).thenReturn("mockJwtToken");

        // Act
        ResponseEntity<String> response = authController.loginUser(createUser(null, null, "google", "google123"));

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("mockJwtToken", response.getBody());  // Expect JWT token as the response
    }

    // Test for OAuth login with non-existent user
    @Test
    public void testLoginUser_OAuthUserNotFound() {
        // Arrange
        when(userService.findUserByProviderAndProviderId("google", "google123")).thenReturn(null);

        // Act
        ResponseEntity<String> response = authController.loginUser(createUser(null, null, "google", "google123"));

        // Assert
        assertEquals(401, response.getStatusCodeValue());
        assertEquals("No user found for the given OAuth credentials", response.getBody());
    }

    // Helper method to create AppUser
    private AppUser createUser(String username, String password, String provider, String providerId) {
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setProvider(provider);
        user.setProviderId(providerId);
        return user;
    }
}