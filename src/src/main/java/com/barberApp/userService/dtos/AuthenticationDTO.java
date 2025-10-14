package com.barberApp.userService.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record AuthenticationDTO(
        @Email(message = "Email inválido")
        @NotNull(message = "Email vazio ou nulo!")
        String email,

        @NotNull(message = "Senha vazia")
        String password
) {
}
