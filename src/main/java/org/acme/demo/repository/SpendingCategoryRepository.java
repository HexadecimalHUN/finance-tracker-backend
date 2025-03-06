package org.acme.demo.repository;

import jakarta.annotation.Nonnull;
import org.acme.demo.entity.AppUser;
import org.acme.demo.entity.SpendingCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpendingCategoryRepository extends JpaRepository<SpendingCategory, Long> {

    /**
     * Find all spending categories for a specific user.
     *
     * @param user the user whose spending categories to retrieve
     * @return a list of spending categories belonging to the user
     */
    List<SpendingCategory> findByUser(@Nonnull AppUser user);

    /**
     * Find a spending category by its name and the associated user.
     *
     * @param name the name of the spending category
     * @param user the user who owns the spending category
     * @return an Optional containing the spending category if found, otherwise empty
     */
    SpendingCategory findByNameAndUser(@Nonnull String name,@Nonnull AppUser user);
}
