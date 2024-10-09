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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
public class SpendingCategoryController {
    @Autowired
    private SpendingCategoryService spendingCategoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private IconService iconService;

    //Get all categories for authenticated user
    @GetMapping
    public ResponseEntity<List<SpendingCategoryDTO>> getUserSpendingCategories(@AuthenticationPrincipal AppUser user){
        List<SpendingCategoryDTO> categories = spendingCategoryService.getUserSpendingCategories(user)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categories);
    };

    //Creating new category
    @PostMapping
    public ResponseEntity<SpendingCategoryDTO> addSpendingCategory(@AuthenticationPrincipal AppUser user, @RequestBody SpendingCategoryDTO categoryDTO){
        Icon icon = iconService.getIconByName(categoryDTO.getIconName());
        SpendingCategory createdCategory = spendingCategoryService.addSpendingCategory(user, categoryDTO.getName(), icon.getIconName());
        return ResponseEntity.ok(convertToDTO(createdCategory));

    }

    //Update category name and icon
    @PutMapping("/{categoryId}")
    public ResponseEntity<SpendingCategoryDTO> updateSpendingCategory(@AuthenticationPrincipal AppUser user, @PathVariable Long categoryId, @RequestBody SpendingCategoryDTO categoryDTO){
        SpendingCategory updatedCategory = spendingCategoryService.updateSpendingCategory(categoryId, categoryDTO, user);
        return ResponseEntity.ok(convertToDTO(updatedCategory));
    }

    //Delete category
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void>deleteSpendingCategory(@AuthenticationPrincipal AppUser user, @PathVariable Long categoryId){
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
