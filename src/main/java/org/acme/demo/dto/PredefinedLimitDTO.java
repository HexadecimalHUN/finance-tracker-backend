package org.acme.demo.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class PredefinedLimitDTO {
    private Long id;
    private BigDecimal amount;
    private String title;
    private String description;

    public PredefinedLimitDTO(){};

    public PredefinedLimitDTO(Long id, BigDecimal amount, String title ,String description){
        this.id = id;
        this.amount = amount;
        this.title = title;
        this.description = description;
    }
}
