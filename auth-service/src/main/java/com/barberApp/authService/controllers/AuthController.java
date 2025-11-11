package com.barberApp.authService.controllers;

import com.barberApp.authService.dtos.*;
import com.barberApp.authService.infra.security.JwtUtils;
import com.barberApp.authService.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtils jwtUtils;

    public AuthController(AuthService authService, JwtUtils jwtUtils) {
        this.authService = authService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody @Valid AuthenticationDTO dto) throws AuthenticationException {
        var token = authService.login(dto);
        return ResponseEntity.ok(new ResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@RequestBody @Valid RegisterDTO dto) {
        authService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/confirm-account")
    public ResponseEntity<ResponseDTO> confirmAccount(@RequestParam("token") String token) {
        authService.confirmAccount(token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/request-confirmation")
    public ResponseEntity<?> requestConfirmation(@RequestParam UUID id) {
        authService.requestConfirmation(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordDTO dto) {
        authService.forgotPassword(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestBody @Valid ResetPasswordDTO dto) {
        var response = authService.resetPassword(token, dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token) {
        if (jwtUtils.boolValidate(token)) {
            return ResponseEntity.ok(new ResponseDTO(token));
        }
        return ResponseEntity.badRequest().build();
    }
}
