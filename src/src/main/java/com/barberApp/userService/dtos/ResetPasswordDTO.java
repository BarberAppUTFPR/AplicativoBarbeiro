package com.barberApp.userService.dtos;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordDTO(
        @NotBlank String new_password
) {
}
