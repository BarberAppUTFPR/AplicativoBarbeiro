package com.barberApp.authService.user_cases.validations;

import com.barberApp.authService.dtos.AuthenticationDTO;
import com.barberApp.authService.infra.exception_handler.exceptions.EmailIsntConfirmed;
import com.barberApp.authService.models.User;
import com.barberApp.authService.repositories.UserRepository;
import com.barberApp.authService.user_cases.interfaces.UserDataValidator;
import org.springframework.stereotype.Component;

@Component
public class EmailConfirmation implements UserDataValidator {
    private final UserRepository userRepository;

    public EmailConfirmation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void validate(AuthenticationDTO authDTO) {
        User user = userRepository.findByEmail(authDTO.email()).orElse(null);
        if (user == null) {
            throw new RuntimeException("E-mail does not exist");
        } else if (!user.isEmailConfirmed()) {
            throw new EmailIsntConfirmed("E-mail isn't confirmed");
        }
    }
}
