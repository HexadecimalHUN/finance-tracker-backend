package org.acme.demo.controller;

import jakarta.validation.Valid;
import org.acme.demo.dto.CurrencyUpdateRequestDTO;
import org.acme.demo.dto.EmailUpdateRequestDTO;
import org.acme.demo.dto.PasswordUpdateRequestDTO;
import org.acme.demo.dto.UsernameUpdateRequestDTO;
import org.acme.demo.entity.AppUser;
import org.acme.demo.security.JwtUtil;
import org.acme.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/settings")
@Validated
public class SettingsController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PutMapping("/username")
    public ResponseEntity<?> updateUsername(@RequestBody @Valid UsernameUpdateRequestDTO payload, Principal principal){
        try {
            String oldUsername = principal.getName();
            String newUsername = payload.getNewUsername();
            AppUser updatedUser = userService.changeUsername(oldUsername, newUsername);
            String newToken = jwtUtil.generateToken(updatedUser.getUsername(), updatedUser.getRoles(), 3600000L);

            return ResponseEntity.ok(Map.of("newToken", newToken));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    };

    @PutMapping("/email")
    public ResponseEntity<?> updateEmail(@RequestBody @Valid EmailUpdateRequestDTO payload, Principal principal){
        try {
            return ResponseEntity.ok(userService.changeEmail(principal.getName(), payload.getNewEmail()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    };

    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(@RequestBody @Valid PasswordUpdateRequestDTO payload, Principal principal){
        try {
            AppUser updatedUser = userService.changePassword(principal.getName(), payload.getCurrentPassword(), payload.getNewPassword());
            String newToken = jwtUtil.generateToken(updatedUser.getUsername(), updatedUser.getRoles(), 3600000L);
            return ResponseEntity.ok(Map.of("newToken", newToken));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    };

    @PutMapping("/currency")
    public ResponseEntity<?> updateCurrency(@RequestBody @Valid CurrencyUpdateRequestDTO payload, Principal principal){
        try {
            return ResponseEntity.ok(userService.setPrimaryCurrency(principal.getName(), payload.getPrimaryCurrency()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/populate")
    public ResponseEntity<Map<String, String>>getUserDetails(Principal principal){
        AppUser user = userService.findUserByUsername(principal.getName());
        if(user == null){
            return ResponseEntity.notFound().build();
        }
        Map<String, String>userDetails = new HashMap<>();
        userDetails.put("username", user.getUsername());
        userDetails.put("email", user.getEmail());
        userDetails.put("primaryCurrency", user.getPrimaryCurrency());

        return ResponseEntity.ok(userDetails);
    }
}
