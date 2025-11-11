package com.barberApp.scheduleService.enums;

import lombok.Getter;

@Getter
public enum User_Role {
    ADMIN("Administrador"), USER("Usuário"), BARBER("Barbeiro");

    private final String role;

    User_Role(String role) {
        this.role = role;
    }
}
