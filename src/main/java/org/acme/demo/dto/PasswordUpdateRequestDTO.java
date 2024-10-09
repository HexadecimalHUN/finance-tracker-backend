package org.acme.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordUpdateRequestDTO {
    @NotBlank(message = "Current password can not be blank")
    private String currentPassword;

    @NotBlank(message = "New password can not be blank")
    private String newPassword;
}
