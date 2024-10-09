package org.acme.demo.repository;

import org.acme.demo.entity.SpendingCategory;
import org.acme.demo.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findBySpendingCategory(SpendingCategory spendingCategory);

    List<Transaction> findBySpendingCategoryAndDateBetween(SpendingCategory spendingCategory, LocalDate startDate, LocalDate endDate);
 }
