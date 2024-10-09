package org.acme.demo.controller;

import org.acme.demo.entity.AppUser;
import org.acme.demo.security.JwtUtil;
import org.acme.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody AppUser user) {
        try {
            AppUser registeredUser = userService.registerUser(
                    user.getUsername(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getProvider(),
                    user.getProviderId()
            );
            String token = jwtUtil.generateToken(registeredUser.getUsername(), registeredUser.getRoles(), 3600000);

            //Creating proper response entity
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Registration Successful");
            response.put("token", token);
            response.put("user", registeredUser);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody AppUser user) {
        //OAuth Login
        if(user.getProvider() != null && user.getProviderId() != null){
            AppUser existingUser = userService.findUserByProviderAndProviderId(user.getProvider(), user.getProviderId());
            if (existingUser == null){
                return ResponseEntity.status(401).body("No user found for the given OAuth credentials");
            }
            String token = jwtUtil.generateToken(existingUser.getUsername(), existingUser.getRoles(), 3600000);
            return ResponseEntity.ok(token);

        }

        AppUser existingUser = userService.findUserByUsername(user.getUsername());
        if (existingUser == null || !passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
        String token = jwtUtil.generateToken(existingUser.getUsername(), existingUser.getRoles(), 3600000);
        return ResponseEntity.ok(token);
    }
}
