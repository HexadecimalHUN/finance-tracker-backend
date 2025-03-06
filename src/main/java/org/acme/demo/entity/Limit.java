package org.acme.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "spending_limit")
public class Limit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private AppUser user;

    @Column(nullable = false)
    private boolean isPredefined = false;

    @Column(name = "predefined_id", nullable = true)
    private Long predefinedId;

    public Limit() {}

    //Base constructor, when creating a custom limit
    public Limit(BigDecimal amount, AppUser user){
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Limit amount must be greater than zero");
        }
        if (user == null){
            throw new IllegalArgumentException("User can not be null");
        }
        this.amount = amount;
        this.user = user;
        this.isPredefined = false;
    }

    //Overloaded constructor for predefined limits
    public Limit(BigDecimal amount, AppUser user, boolean isPredefined, Long predefinedId){
        this(amount,  user);
        this.isPredefined = isPredefined;
        this.predefinedId = predefinedId;
    }

    public boolean getIsPredefined(){
        return isPredefined;
    }

    public void setPredefinedLimit(Long predefinedId){
        this.predefinedId = predefinedId;
        this.isPredefined = (predefinedId != null);
    }
}
