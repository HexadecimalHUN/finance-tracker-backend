package org.acme.demo.security;

import org.acme.demo.entity.Role;
import org.acme.demo.entity.RoleName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Setup your JwtUtil (file paths should already be handled within JwtUtil)
    }

    @Test
    public void testGenerateAndValidateToken() {
        // Prepare data
        String username = "testuser";
        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        role.setName(RoleName.ROLE_USER);
        roles.add(role);

        // Generate Token
        String token = jwtUtil.generateToken(username, roles, 1000);
        assertNotNull(token, "JWT token should not be null");

        // Extract Username
        String extractedUsername = jwtUtil.extractUsername(token);
        assertEquals(username, extractedUsername, "Extracted username should match the original username");

        // Validate Token
        boolean isValid = jwtUtil.validateToken(token, username);
        assertTrue(isValid, "Token should be valid");
    }

    @Test
    public void testIsTokenExpired() {
        // Prepare data
        String username = "testuser";
        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        role.setName(RoleName.ROLE_USER);
        roles.add(role);

        // Generate a token that will expire in 1 second for the sake of the test
        String token = jwtUtil.generateToken(username, roles, 1000); // 1 second expiration
        assertNotNull(token, "JWT token should not be null");

        // Sleep to let the token expire
        try {
            Thread.sleep(2000); // Wait 2 seconds to ensure the token is expired
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Check if the token is expired
        try {
            boolean isExpired = jwtUtil.isTokenExpired(token);
            assertTrue(isExpired, "Token should be expired");
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            // Token has expired as expected
            assertTrue(true, "Token is expired");
        } catch (Exception e) {
            fail("An unexpected exception occurred: " + e.getMessage());
        }
    }
}