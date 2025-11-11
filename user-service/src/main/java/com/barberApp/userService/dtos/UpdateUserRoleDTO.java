package com.barberApp.userService.dtos;

import com.barberApp.userService.enums.User_Role;
import jakarta.validation.constraints.NotNull;

public record UpdateUserRoleDTO(
        @NotNull(message = "Role is required")
        User_Role role
) {
}
