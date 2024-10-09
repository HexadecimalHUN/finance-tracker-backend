package org.acme.demo.service;

import org.acme.demo.entity.AppUser;
import org.acme.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRegisterUser_SuccessfulRegistration() {
        // Arrange
        String username = "testuser";
        String email = "testuser@example.com";
        String password = "password";
        String encodedPassword = "encodedPassword";
        AppUser savedUser = new AppUser();
        savedUser.setUsername(username);
        savedUser.setEmail(email);
        savedUser.setPassword(encodedPassword);

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(userRepository.save(any(AppUser.class))).thenReturn(savedUser);

        // Act
        AppUser result = userService.registerUser(username, email, password, null, null);

        // Assert
        assertEquals(username, result.getUsername());
        assertEquals(email, result.getEmail());
        assertEquals(encodedPassword, result.getPassword());
        verify(userRepository, times(1)).save(any(AppUser.class));
    }

    @Test
    public void testRegisterUser_UsernameAlreadyTaken() {
        // Arrange
        String username = "testuser";
        String email = "testuser@example.com";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new AppUser()));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(username, email, "password", null, null);
        });

        assertEquals("Username already taken", exception.getMessage());
    }

    @Test
    public void testRegisterUser_EmailAlreadyRegistered() {
        // Arrange
        String username = "testuser";
        String email = "testuser@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new AppUser()));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(username, email, "password", null, null);
        });

        assertEquals("Email is already registered", exception.getMessage());
    }

    @Test
    void testRegisterUser_EmailAlreadyRegisteredWithOAuth() {
        // Setup: Existing user with OAuth details
        AppUser existingUser = new AppUser();
        existingUser.setEmail("user@example.com");
        existingUser.setProvider("google");
        existingUser.setProviderId("google123");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(existingUser));

        // Call registerUser for OAuth scenario with matching provider
        AppUser result = userService.registerUser(null, "user@example.com", null, "google", "google123");

        // Verify: No new user is created, existing user is returned
        assertEquals(existingUser, result);
        verify(userRepository, never()).save(any());
    }

    @Test
    void testRegisterUser_EmailAlreadyRegisteredWithDifferentMethod() {
        // Setup: Existing user with standard registration
        AppUser existingUser = new AppUser();
        existingUser.setEmail("user@example.com");
        existingUser.setPassword(passwordEncoder.encode("password123"));  // Non-OAuth registration
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(existingUser));

        // Call registerUser with OAuth details (expect failure)
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                userService.registerUser(null, "user@example.com", null, "facebook", "fb123")
        );

        // Verify: Exception message should be correct
        assertEquals("Email is already registered with a different method", exception.getMessage());
    }

    @Test
    public void testRegisterUser_DefaultUsernameGeneration() {
        // Arrange
        String email = "testuser@example.com";
        String password = "password";
        String encodedPassword = "encodedPassword";
        AppUser savedUser = new AppUser();
        savedUser.setUsername("testuser");
        savedUser.setEmail(email);
        savedUser.setPassword(encodedPassword);

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(userRepository.save(any(AppUser.class))).thenReturn(savedUser);

        // Act
        AppUser result = userService.registerUser(null, email, password, null, null);

        // Assert
        assertEquals("testuser", result.getUsername());
        assertEquals(email, result.getEmail());
        assertEquals(encodedPassword, result.getPassword());
    }

    @Test
    public void testRegisterUser_RegistrationWithOAuth() {
        // Arrange
        String username = "testuser";
        String email = "testuser@example.com";
        String provider = "google";
        String providerId = "12345";

        AppUser savedUser = new AppUser();
        savedUser.setUsername(username);
        savedUser.setEmail(email);
        savedUser.setProvider(provider);
        savedUser.setProviderId(providerId);

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(userRepository.save(any(AppUser.class))).thenReturn(savedUser);

        // Act
        AppUser result = userService.registerUser(username, email, null, provider, providerId);

        // Assert
        assertEquals(username, result.getUsername());
        assertEquals(email, result.getEmail());
        assertNull(result.getPassword());
        assertEquals(provider, result.getProvider());
        assertEquals(providerId, result.getProviderId());
    }

    @Test
    public void testFindUserByEmail_UserExists() {
        // Arrange
        String email = "testuser@example.com";
        AppUser user = new AppUser();
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        AppUser result = userService.findUserByEmail(email);

        // Assert
        assertEquals(user, result);
    }

    @Test
    public void testFindUserByEmail_UserDoesNotExist() {
        // Arrange
        String email = "testuser@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act
        AppUser result = userService.findUserByEmail(email);

        // Assert
        assertNull(result);
    }

    @Test
    public void testFindUserByUsername_UserExists() {
        // Arrange
        String username = "testuser";
        AppUser user = new AppUser();
        user.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        AppUser result = userService.findUserByUsername(username);

        // Assert
        assertEquals(user, result);
    }

    @Test
    public void testFindUserByUsername_UserDoesNotExist() {
        // Arrange
        String username = "testuser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act
        AppUser result = userService.findUserByUsername(username);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGenerateDefaultUsername_WithValidEmail() {
        // Act
        String result = userService.generateDefaultUsername("testuser@example.com");

        // Assert
        assertEquals("testuser", result);
    }

    @Test
    public void testGenerateDefaultUsername_WithInvalidEmail() {
        // Act
        String result = userService.generateDefaultUsername("invalidEmail");

        // Assert
        assertTrue(result.startsWith("user"));
    }
}