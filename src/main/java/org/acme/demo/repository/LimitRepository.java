package org.acme.demo.repository;


import org.acme.demo.entity.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LimitRepository extends JpaRepository<Limit, Long> {

    /**
     * Find the limit by the user ID.
     *
     * @param userId the ID of the user associated with the limit
     * @return the user's spending limit, if present
     */
    Limit findByUserId(Long userId);

    /**
     * Find a predefined limit by predefinedId.
     *
     * @param predefinedId the ID of the predefined limit
     * @return an Optional containing the predefined limit if found
     */
    Optional<Limit> findByPredefinedId(Long predefinedId);

    /**
     * Check if a limit is predefined by the user ID.
     *
     * @param userId the ID of the user associated with the limit
     * @return true if the limit is predefined, otherwise false
     */
    boolean existsByUserIdAndIsPredefinedTrue(Long userId);
}
