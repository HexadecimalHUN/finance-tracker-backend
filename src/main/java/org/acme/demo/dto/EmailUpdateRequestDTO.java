package org.acme.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailUpdateRequestDTO {
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email can not be blank")
    private String newEmail;
}
