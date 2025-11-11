package com.barberApp.authService.user_cases.interfaces;

import com.barberApp.authService.dtos.AuthenticationDTO;

public interface UserDataValidator {
    void validate(AuthenticationDTO authDTO);
}
