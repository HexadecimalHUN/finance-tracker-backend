package org.acme.demo.controller;

import org.acme.demo.dto.LimitDTO;
import org.acme.demo.entity.AppUser;
import org.acme.demo.entity.Limit;
import org.acme.demo.entity.PredefinedLimit;
import org.acme.demo.service.LimitService;
import org.acme.demo.service.PredefinedLimitService;
import org.acme.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;

@RestController
@RequestMapping("/limit")
public class LimitController {
    private final LimitService limitService;
    private final UserService userService;
    private final PredefinedLimitService predefinedLimitService;

    @Autowired
    public LimitController(LimitService limitService, UserService userService, PredefinedLimitService predefinedLimitService){
        this.limitService = limitService;
        this.userService = userService;
        this.predefinedLimitService = predefinedLimitService;
    }

    @GetMapping
    public ResponseEntity<LimitDTO> getUserLimit(Principal principal){
        AppUser user = userService.findUserByUsername(principal.getName());
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        Limit limit = limitService.getUserLimit(user);
        if (limit == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDTO(limit));
    }

    @PostMapping
    public ResponseEntity<LimitDTO> setUserLimit (Principal principal, @RequestBody LimitDTO limitDTO){
        AppUser user = userService.findUserByUsername(principal.getName());
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
       Limit updatedLimit;
        if(Boolean.TRUE.equals(limitDTO.getIsPredefined()) && limitDTO.getPredefinedId() != null){
            PredefinedLimit predefinedLimit = predefinedLimitService.findById(limitDTO.getPredefinedId()).orElse(null);
            if(predefinedLimit == null){
                return ResponseEntity.badRequest().build();
            }
            updatedLimit = limitService.setPredefinedLimit(user, limitDTO.getPredefinedId());
        }else{
            updatedLimit = limitService.setOrUpdateCustomLimit(user, limitDTO.getAmount());
        }
        return ResponseEntity.ok(convertToDTO(updatedLimit));
    }

    @DeleteMapping
    public ResponseEntity<Void>deleteUserLimit(Principal principal){
        AppUser user = userService.findUserByUsername(principal.getName());
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        limitService.deleteLimit(user);
        return ResponseEntity.noContent().build();
    }

    private LimitDTO convertToDTO(Limit limit) {
        return new LimitDTO(
                limit.getId(),
                limit.getAmount(),
                limit.getUser().getId(),
                limit.getIsPredefined(),
                limit.getPredefinedId()
        );
    }
}
