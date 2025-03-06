package org.acme.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class PlanDTO {
    private Long id;
    private String name;
    private BigDecimal amount;
    private int recurrenceIntervalMonths;
    private LocalDate startDate;
    private LocalDate nextTransactionDate;
    private Long categoryId;

    public PlanDTO(){};

    public PlanDTO(Long id, String name, BigDecimal amount, int recurrenceIntervalMonths, LocalDate startDate, LocalDate nextTransactionDate, Long categoryId){
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.recurrenceIntervalMonths = recurrenceIntervalMonths;
        this.startDate = startDate;
        this.nextTransactionDate = nextTransactionDate;
        this.categoryId = categoryId;
    }
}
