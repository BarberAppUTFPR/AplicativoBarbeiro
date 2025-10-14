package com.barberApp.userService.user_cases.validations.user;

import com.barberApp.userService.dtos.AuthenticationDTO;
import com.barberApp.userService.infra.exception_handler.exceptions.EmailIsntConfirmed;
import com.barberApp.userService.models.User;
import com.barberApp.userService.services.GenericUserService;
import com.barberApp.userService.user_cases.interfaces.user.UserDataValidator;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class EmailConfirmation implements UserDataValidator {
    private final GenericUserService genericUserService;

    public EmailConfirmation(@Lazy GenericUserService genericUserService) {
        this.genericUserService = genericUserService;
    }

    @Override
    public void validate(AuthenticationDTO authDTO) {
        User user = genericUserService.findByEmail(authDTO.email());
        if (user == null) {
            throw new RuntimeException("E-mail does not exist");
        } else if (!user.isEmailConfirmed()) {
            throw new EmailIsntConfirmed("E-mail isn't confirmed");
        }
    }
}
