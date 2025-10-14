package com.barberApp.userService.dtos;


import com.barberApp.userService.models.User;

public record GetUsersDTO(
        String name,
        String email,
        String phone
) {

    public GetUsersDTO(User user) {
        this(user.getName(), user.getEmail(), user.getPhone());
    }
}
