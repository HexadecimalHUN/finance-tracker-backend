package org.acme.demo.controller;

import org.acme.demo.dto.PlanDTO;
import org.acme.demo.entity.AppUser;
import org.acme.demo.entity.Plan;
import org.acme.demo.entity.SpendingCategory;
import org.acme.demo.service.PlanService;
import org.acme.demo.service.SpendingCategoryService;
import org.acme.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/plan")
public class PlanController {

    private final PlanService planService;
    private final UserService userService;
    private final SpendingCategoryService spendingCategoryService;

    @Autowired
    public PlanController(PlanService planService, UserService userService, SpendingCategoryService spendingCategoryService){
        this.planService = planService;
        this.userService = userService;
        this.spendingCategoryService = spendingCategoryService;
    }

    //Get all plans for authenticated user
    @GetMapping
    public ResponseEntity<List<PlanDTO>> getUserPlans(Principal principal){
        AppUser user= userService.findUserByUsername(principal.getName());
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        List<PlanDTO> plans = planService.getUserPlans(user).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(plans);

    }

    //Add new plan
    public ResponseEntity<PlanDTO> addPlan(
            Principal principal,
            @RequestParam String name,
            @RequestParam BigDecimal amount,
            @RequestParam int recurrenceInterval,
            @RequestParam LocalDate startDate,
            @RequestParam Long categoryID
            ){
        AppUser user = userService.findUserByUsername(principal.getName());
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        SpendingCategory category = spendingCategoryService.getUserSpendingCategories(user).stream()
                .filter(sc -> sc.getId().equals(categoryID))
                .findFirst()
                .orElseThrow(()-> new IllegalArgumentException("Invalid category ID for the user."));

        Plan newPlan = planService.addPlan(user, name, amount, recurrenceInterval, startDate, category);
        return ResponseEntity.ok(convertToDTO(newPlan));
    }

    //Update an existing plan
    @PutMapping("/{planId}")
    public ResponseEntity<PlanDTO> updatePlan(
            Principal principal,
            @PathVariable Long planId,
            @RequestParam String name,
            @RequestParam BigDecimal amount,
            @RequestParam int recurrenceInterval,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate nextTransactionDate,
            @RequestParam Long categoryId
    ){
        AppUser user = userService.findUserByUsername(principal.getName());
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        SpendingCategory category = spendingCategoryService.getUserSpendingCategories(user).stream()
                .filter(sc -> sc.getId().equals(categoryId))
                .findFirst()
                .orElseThrow(()-> new IllegalArgumentException("Invalid category id for the user."));

        Plan updatePlan = planService.updatePlan(planId, name, amount, recurrenceInterval, startDate, nextTransactionDate, category)
                .orElseThrow(()-> new IllegalArgumentException("Plan not found"));
        return ResponseEntity.ok(convertToDTO(updatePlan));
    }

    //Delete plan by ID
    @DeleteMapping("/{planId}")
    public ResponseEntity<Void> deletePlan(@PathVariable Long planId){
        planService.deletePlan(planId);
        return ResponseEntity.noContent().build();
    }

    private PlanDTO convertToDTO(Plan plan){
        return new PlanDTO(
                plan.getId(),
                plan.getName(),
                plan.getAmount(),
                plan.getRecurrenceIntervalMonths(),
                plan.getStartDate(),
                plan.getNextTransactionDate(),
                plan.getCategory().getId()
        );
    }
}
