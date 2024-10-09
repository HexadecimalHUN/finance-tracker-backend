package org.acme.demo.dto;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class UsernameUpdateRequestDTO {
    @NotBlank(message = "Username can not be blank")
    private String newUsername;
}
