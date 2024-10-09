package org.acme.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrencyUpdateRequestDTO {
    @NotBlank(message = "Primary currency can not be blank")
    private String primaryCurrency;
}
