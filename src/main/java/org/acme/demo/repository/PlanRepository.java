package org.acme.demo.repository;

import org.acme.demo.entity.AppUser;
import org.acme.demo.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

    /**
     * Find all plans associated with a specific user.
     *
     * @param user the user whose plans to retrieve
     * @return a list of plans for the given user
     */
    List<Plan> findByUser(AppUser user);

    /**
     * Find all plans where the next transaction date is before or on a specified date.
     *
     * @param date the cutoff date for upcoming transactions
     * @return a list of plans with next transaction dates before or on the specified date
     */
    List<Plan> findByNextTransactionDateLessThanEqual(LocalDate date);
}
