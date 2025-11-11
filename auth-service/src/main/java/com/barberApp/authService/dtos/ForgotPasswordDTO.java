package com.barberApp.authService.dtos;

import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordDTO(@NotBlank String email) {
}
