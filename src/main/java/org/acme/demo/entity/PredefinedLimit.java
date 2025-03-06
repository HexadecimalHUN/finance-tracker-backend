package org.acme.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "predefined_limit")
@Getter
@Setter
public class PredefinedLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    public PredefinedLimit(){};

    public PredefinedLimit(BigDecimal amount, String title, String description){
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Predefined limit amount must be greater than 0");
        }

        if (title == null || title.isEmpty()){
            throw new IllegalArgumentException("Title can not be null or empty");
        }


        if (description == null || description.isEmpty()){
            throw new IllegalArgumentException("Description can not be null or empty");
        }


        this.title = title;
        this.amount =  amount;
        this.description = description;
    }
}
