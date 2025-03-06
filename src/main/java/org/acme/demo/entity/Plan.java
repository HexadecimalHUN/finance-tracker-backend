package org.acme.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="plan")
@Getter
@Setter
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private int recurrenceIntervalMonths;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate nextTransactionDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private SpendingCategory category;

    public Plan(){}

    public Plan(String name, BigDecimal amount, int recurrenceIntervalMonths, LocalDate startDate, LocalDate nextTransactionDate, AppUser user, SpendingCategory category){
        this.name = name;
        this.amount = amount;
        this.recurrenceIntervalMonths = recurrenceIntervalMonths;
        this.startDate = startDate;
        this.nextTransactionDate = nextTransactionDate;
        this.user = user;
        this.category = category;
    }

    public void updateNextTransactionDate(){
        this.nextTransactionDate = this.nextTransactionDate.plusMonths(this.recurrenceIntervalMonths);
    }

}
