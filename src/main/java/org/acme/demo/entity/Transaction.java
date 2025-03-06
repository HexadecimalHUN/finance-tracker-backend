package org.acme.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table (name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate date;

    @Column(length = 255)
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private SpendingCategory spendingCategory;

    public Transaction(){}

    public Transaction(BigDecimal amount, LocalDate date,String description, SpendingCategory spendingCategory){
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        if (spendingCategory == null) {
            throw new IllegalArgumentException("Spending Category cannot be null");
        }
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.spendingCategory = spendingCategory;

    }
}
