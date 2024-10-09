package org.acme.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate date;

    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private SpendingCategory spendingCategory;

    public Transaction(){}

    public Transaction(BigDecimal amount, LocalDate date,String description, SpendingCategory spendingCategory){
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.spendingCategory = spendingCategory;

    }
}
