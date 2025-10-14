package com.barberApp.userService.user_cases.interfaces.user;


import com.barberApp.userService.dtos.AuthenticationDTO;

public interface UserDataValidator {
    void validate(AuthenticationDTO authDTO);
}
