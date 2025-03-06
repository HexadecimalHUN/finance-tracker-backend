package org.acme.demo.controller;

import org.acme.demo.dto.SpendingCategoryDTO;
import org.acme.demo.entity.AppUser;
import org.acme.demo.entity.Icon;
import org.acme.demo.entity.SpendingCategory;
import org.acme.demo.service.IconService;
import org.acme.demo.service.SpendingCategoryService;
import org.acme.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
public class SpendingCategoryController {

    private final SpendingCategoryService spendingCategoryService;
    private final UserService userService;
    private final IconService iconService;

    @Autowired
    public SpendingCategoryController(SpendingCategoryService spendingCategoryService, UserService userService, IconService iconService){
        this.spendingCategoryService = spendingCategoryService;
        this.userService = userService;
        this.iconService = iconService;
    }



    //Get all categories for authenticated user
    @GetMapping
    public ResponseEntity<List<SpendingCategoryDTO>> getUserSpendingCategories(Principal principal){
        AppUser user = userService.findUserByUsername(principal.getName());
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        List<SpendingCategoryDTO> categories = spendingCategoryService.getUserSpendingCategories(user)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categories);
    };

    //Creating new category
    @PostMapping
    public ResponseEntity<SpendingCategoryDTO> addSpendingCategory(Principal principal, @RequestBody SpendingCategoryDTO categoryDTO){
        System.out.println("Received DTO: " + categoryDTO.getName() + ", " + categoryDTO.getIconName());
        AppUser user = userService.findUserByUsername(principal.getName());
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        Icon icon = iconService.getIconByName(categoryDTO.getIconName());
        SpendingCategory createdCategory = spendingCategoryService.addSpendingCategory(user, categoryDTO.getName(), icon.getIconName());
        return ResponseEntity.ok(convertToDTO(createdCategory));

    }

    //Update category name and icon
    @PutMapping("/{categoryId}")
    public ResponseEntity<SpendingCategoryDTO> updateSpendingCategory(Principal principal, @PathVariable Long categoryId, @RequestBody SpendingCategoryDTO categoryDTO){
        AppUser user = userService.findUserByUsername(principal.getName());
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        SpendingCategory updatedCategory = spendingCategoryService.updateSpendingCategory(categoryId, categoryDTO, user);
        return ResponseEntity.ok(convertToDTO(updatedCategory));
    }

    //Delete category
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void>deleteSpendingCategory(Principal principal, @PathVariable Long categoryId){
        AppUser user = userService.findUserByUsername(principal.getName());
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        spendingCategoryService.deleteSpendingCategory(categoryId, user);
        return ResponseEntity.noContent().build();
    }

    private SpendingCategoryDTO convertToDTO(SpendingCategory category){
        SpendingCategoryDTO dto = new SpendingCategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setIconId(category.getIcon().getId());
        dto.setIconName(category.getIcon().getIconName());
        return dto;
    }
}
