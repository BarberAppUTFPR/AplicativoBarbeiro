package com.barberApp.userService.controllers;


import com.barberApp.userService.dtos.*;
import com.barberApp.userService.infra.security.JwtUtils;
import com.barberApp.userService.services.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {


    private final UserService userService;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthController(UserService userService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }


    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody @Valid AuthenticationDTO dto) throws AuthenticationException {
        var token = userService.login(dto);
        return ResponseEntity.ok(new ResponseDTO(token));
    }

    @GetMapping("/login")
    public ResponseEntity<ResponseDTO> login() {
        return ResponseEntity.ok().build();
    }


    @PostMapping("/register")
    @Transactional
    public ResponseEntity<ResponseDTO> register(@RequestBody @Valid RegisterDTO dto) {
        userService.Create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/update")
    @Transactional
    public ResponseEntity<ResponseDTO> Update(@RequestBody @Valid UpdateDTO dto, @RequestParam UUID id) {
        userService.Update(dto, id);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDTO> delete(@RequestParam UUID id) {
        userService.Delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/confirm-account")
    public ResponseEntity<ResponseDTO> confirmAccount(@RequestParam("token") String token) {
        userService.confirmAccount(token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/request-confirmation")
    public ResponseEntity<?> requestConfirmation(@RequestParam UUID id) {
        userService.requestConfirmation(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordDTO dto) {
        userService.forgotPassword(userService.findUserByEmail(dto.email()));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestBody @Valid ResetPasswordDTO dto) {
        var response = userService.resetPassword(token, dto);
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
