package com.barberApp.authService.user_cases.interfaces;

import com.barberApp.authService.dtos.RegisterDTO;

public interface UserEmailValidator {
    void validate(RegisterDTO registerDTO);
}
