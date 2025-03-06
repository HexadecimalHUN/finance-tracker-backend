package org.acme.demo.service;

import org.acme.demo.entity.AppUser;
import org.acme.demo.entity.Plan;
import org.acme.demo.entity.SpendingCategory;
import org.acme.demo.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PlanService {
    private final PlanRepository planRepository;

    @Autowired
    public PlanService(PlanRepository planRepository){
        this.planRepository = planRepository;
    }

    /**
     * Retrieves all plans for a specific user.
     *
     * @param user the user whose plans to retrieve
     * @return a list of plans associated with the user
     */
    public List<Plan> getUserPlans(AppUser user){
        return planRepository.findByUser(user);
    }

    /**
     * Adds a new subscription plan for a user.
     *
     * @param user               the user who owns the plan
     * @param name               the name of the plan
     * @param amount             the recurring amount
     * @param recurrenceInterval the recurrence interval in months
     * @param startDate           the starting date of the subscription
     * @param category           the spending category for the plan
     * @return the saved plan
     */

    public Plan addPlan(AppUser user, String name, BigDecimal amount, int recurrenceInterval,LocalDate startDate, SpendingCategory category){
        LocalDate nextTransactionDate = startDate;
        Plan plan = new Plan(name, amount, recurrenceInterval, startDate, nextTransactionDate, user, category);
        return planRepository.save(plan);
    }

    /**
     * Updates an existing plan.
     *
     * @param planId             the ID of the plan to update
     * @param name               the new name of the plan
     * @param amount             the new recurring amount
     * @param recurrenceInterval the new recurrence interval in months
     * @param startDate           the starting date of the subscription
     * @param nextTransactionDate the new next transaction date
     * @param category           the updated spending category
     * @return the updated plan
     */

    public Optional<Plan> updatePlan(Long planId, String name, BigDecimal amount, int recurrenceInterval, LocalDate startDate, LocalDate nextTransactionDate, SpendingCategory category){
        return planRepository.findById(planId).map(plan -> {
            plan.setName(name);
            plan.setAmount(amount);
            plan.setRecurrenceIntervalMonths(recurrenceInterval);
            plan.setStartDate(startDate);
            plan.setNextTransactionDate(nextTransactionDate);
            plan.setCategory(category);
            return planRepository.save(plan);
        });
    }

    /**
     * Deletes a plan by ID.
     *
     * @param planId the ID of the plan to delete
     */

    public void deletePlan(Long planId){
        planRepository.deleteById(planId);
    }

    /**
     * Finds all plans with a next transaction date on or before today,
     * indicating they are due for a recurring transaction.
     *
     * @return a list of plans due for transactions
     */

    public List<Plan> getPlansDueForTransaction(){
        return planRepository.findByNextTransactionDateLessThanEqual(LocalDate.now());
    }

    /**
     * Updates the next transaction date for a plan based on its recurrence interval.
     *
     * @param plan the plan to update
     */

    public void updateNextTransactionDate(Plan plan){
        plan.setNextTransactionDate(plan.getNextTransactionDate().plusMonths(plan.getRecurrenceIntervalMonths()));
        planRepository.save(plan);
    }


}
