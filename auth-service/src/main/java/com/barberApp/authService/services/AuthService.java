package com.barberApp.authService.services;

import com.barberApp.authService.dtos.AuthenticationDTO;
import com.barberApp.authService.dtos.ForgotPasswordDTO;
import com.barberApp.authService.dtos.RegisterDTO;
import com.barberApp.authService.dtos.ResetPasswordDTO;
import com.barberApp.authService.infra.exception_handler.exceptions.EmailAlreadyExistsValidator;
import com.barberApp.authService.infra.exception_handler.exceptions.EmailIsntConfirmed;
import com.barberApp.authService.infra.security.JwtUtils;
import com.barberApp.authService.models.User;
import com.barberApp.authService.repositories.UserRepository;
import com.barberApp.authService.user_cases.interfaces.UserDataValidator;
import com.barberApp.authService.user_cases.interfaces.UserEmailValidator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final List<UserEmailValidator> userEmailValidators;
    private final List<UserDataValidator> userDataValidators;

    public AuthService(UserRepository userRepository,
                      @Lazy AuthenticationManager authenticationManager,
                      JwtUtils jwtUtils,
                      List<UserEmailValidator> userEmailValidators,
                      List<UserDataValidator> userDataValidators) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userEmailValidators = userEmailValidators;
        this.userDataValidators = userDataValidators;
    }

    @Transactional
    public void register(@Valid RegisterDTO dto) {
        try {
            userEmailValidators.forEach(v -> v.validate(dto));
            User newUser = new User();
            newUser.setName(dto.name());
            newUser.setEmail(dto.email());
            newUser.setEmailConfirmed(true);
            newUser.setPhone(dto.phone());
            String encryptPassword = new BCryptPasswordEncoder().encode(dto.password());
            newUser.setPassword(encryptPassword);
            userRepository.save(newUser);
            // TODO: Send confirmation email
        } catch (EmailAlreadyExistsValidator e) {
            throw new EmailAlreadyExistsValidator(e.getMessage());
        }
    }

    public String login(AuthenticationDTO dto) throws AuthenticationException {
        userDataValidators.forEach(validator -> {
            try {
                validator.validate(dto);
            } catch (EmailIsntConfirmed ex) {
                // TODO: Send confirmation email
                throw new EmailIsntConfirmed("Email isn't confirmed");
            } catch (RuntimeException ex) {
                throw new RuntimeException(ex);
            }
        });
        try {
            var usernameAndPassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
            var auth = this.authenticationManager.authenticate(usernameAndPassword);
            return jwtUtils.generateJwt((User) auth.getPrincipal());
        } catch (RuntimeException e) {
            throw new AuthenticationException("Auth failed");
        }
    }

    @Transactional
    public void confirmAccount(String token) {
        try {
            String userEmail = jwtUtils.validate(token);
            var user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new EntityNotFoundException("User with email " + userEmail + " not found."));

            if (user.isEmailConfirmed()) {
                throw new IllegalStateException("E-mail has already been confirmed");
            }

            user.setEmailConfirmed(true);
            userRepository.save(user);

        } catch (IllegalArgumentException | IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Token is invalid", e);
        }
    }

    @Transactional
    public void requestConfirmation(UUID id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found."));
        // TODO: Send confirmation email
    }

    public void forgotPassword(ForgotPasswordDTO dto) {
        var user = userRepository.findByEmail(dto.email()).orElse(null);
        if (user == null) return;
        // TODO: Send forgot password email
    }

    @Transactional
    public Map<String, String> resetPassword(String token, ResetPasswordDTO dto) {
        String newPassword = dto.new_password();

        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("New password is blank");
        }

        var subject = jwtUtils.validate(token).trim();
        var user = userRepository.findByEmail(subject)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + subject + " not found."));

        String encryptPassword = new BCryptPasswordEncoder().encode(newPassword);
        user.setPassword(encryptPassword);
        userRepository.save(user);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Password reset successfully");
        return response;
    }
}
