package org.acme.demo.service;

import jdk.jfr.Category;
import org.acme.demo.entity.SpendingCategory;
import org.acme.demo.entity.Transaction;
import org.acme.demo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction>getTransactionsBySpendingCategory(SpendingCategory spendingCategory){
        return transactionRepository.findBySpendingCategory(spendingCategory);
    }

    public List<Transaction>getTransactionsBySpendingCategoryAndDate(SpendingCategory spendingCategory, LocalDate startDate, LocalDate endDate){
        return transactionRepository.findBySpendingCategoryAndDateBetween(spendingCategory, startDate, endDate);
    }

    public Transaction addTransaction(SpendingCategory spendingCategory, String description, LocalDate date, BigDecimal amount){
        Transaction newTransaction = new Transaction();
        newTransaction.setSpendingCategory(spendingCategory);
        newTransaction.setDescription(description);
        newTransaction.setDate(date); // LocalDate is now expected
        newTransaction.setAmount(amount); // BigDecimal is now expected

        return transactionRepository.save(newTransaction);
    }

    public Transaction updateTransaction(Long transactionId, String newDescription, LocalDate newDate, BigDecimal newAmount){
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(()-> new IllegalArgumentException("Transaction not found"));
        transaction.setDescription(newDescription);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        transaction.setDate(newDate);
        transaction.setAmount(newAmount);
        return transactionRepository.save(transaction);
    }

    public void deleteTransaction(Long transactionId){
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(()-> new IllegalArgumentException("Transaction not found"));
        transactionRepository.delete(transaction);
    }

}
