package com.barberApp.userService.enums;

import lombok.Getter;

@Getter
public enum User_Role {
    ADMIN("admin"), USER("user"), BARBER("barber");


    private final String role;

    User_Role(String role) {
        this.role = role;
    }
}
