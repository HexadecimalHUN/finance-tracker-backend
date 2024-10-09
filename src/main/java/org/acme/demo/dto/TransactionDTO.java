package org.acme.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class TransactionDTO {
    private Long id;
    private String description;
    private BigDecimal amount;
    private LocalDate transactionDate;
    private Long categoryId;
}
