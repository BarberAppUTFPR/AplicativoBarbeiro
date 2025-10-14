package com.barberApp.userService.user_cases.interfaces.user;


import com.barberApp.userService.dtos.RegisterDTO;

public interface UserEmailValidator {
    void validate(RegisterDTO registerDTO);
}
