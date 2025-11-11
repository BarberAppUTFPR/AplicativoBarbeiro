package com.barberApp.authService.user_cases.validations;

import com.barberApp.authService.dtos.RegisterDTO;
import com.barberApp.authService.infra.exception_handler.exceptions.EmailAlreadyExistsValidator;
import com.barberApp.authService.repositories.UserRepository;
import com.barberApp.authService.user_cases.interfaces.UserEmailValidator;
import org.springframework.stereotype.Component;

@Component
public class EmailAlreadyExists implements UserEmailValidator {

    private final UserRepository userRepository;

    public EmailAlreadyExists(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void validate(RegisterDTO registerDTO) {
        if (userRepository.existsByEmail(registerDTO.email())) {
            throw new EmailAlreadyExistsValidator("Email Already exists");
        }
    }
}
