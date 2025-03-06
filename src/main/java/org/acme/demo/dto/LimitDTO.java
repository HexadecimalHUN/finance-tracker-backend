package org.acme.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LimitDTO {
    private Long id;
    private BigDecimal amount;
    private Long userId;
    private Boolean isPredefined;
    private Long predefinedId;

    public LimitDTO(){};

    public LimitDTO(Long id, BigDecimal amount, Long userId, Boolean isPredefined, Long predefinedId){
        this.id = id;
        this.amount = amount;
        this.userId = userId;
        this.isPredefined = isPredefined;
        this.predefinedId = predefinedId;
    }


}
