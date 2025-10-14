package com.barberApp.userService.dtos;

import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordDTO(@NotBlank String email) {
}
