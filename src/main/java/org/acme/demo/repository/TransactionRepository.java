package org.acme.demo.repository;

import jakarta.annotation.Nonnull;
import org.acme.demo.entity.AppUser;
import org.acme.demo.entity.SpendingCategory;
import org.acme.demo.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {


    /**
     * Find all transactions for a specific spending category.
     *
     * @param spendingCategory the spending category whose transactions to retrieve
     * @return a list of transactions belonging to the spending category
     */
    List<Transaction> findBySpendingCategory(@Nonnull SpendingCategory spendingCategory);

    /**
     * Find all transactions for a specific spending category within a date range.
     *
     * @param spendingCategory the spending category whose transactions to retrieve
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     * @return a list of transactions in the spending category between the specified dates
     */
    List<Transaction> findBySpendingCategoryAndDateBetween(@Nonnull SpendingCategory spendingCategory,@Nonnull LocalDate startDate,@Nonnull LocalDate endDate);

    /**
     * Find all transactions for a specific user within a date range.
     *
     * @param user the user whose transactions to retrieve
     * @param start the start date of the range
     * @param end the end date of the range
     * @return a list of transactions for the user between the specified dates
     */
    @Query("SELECT t from Transaction t WHERE t.spendingCategory.user = :user AND t.date BETWEEN :start AND :end")
    List<Transaction> findByUserAndDateRange(@Param("user")AppUser user, @Param("start")LocalDate start, @Param("end")LocalDate end);
 }
