package com.barberApp.userService.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UpdateDTO(
        String name,
        String email,
        String phone
) {
}
