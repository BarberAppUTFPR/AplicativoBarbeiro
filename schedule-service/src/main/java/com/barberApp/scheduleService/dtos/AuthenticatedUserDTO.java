package com.barberApp.scheduleService.dtos;

import java.util.UUID;

public record AuthenticatedUserDTO(
        UUID id,
        String email,
        String role
) { }
