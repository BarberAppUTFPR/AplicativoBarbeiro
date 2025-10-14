package com.barberApp.userService.infra.exception_handler;

import com.barberApp.userService.infra.exception_handler.exceptions.EmailAlreadyExistsValidator;
import com.barberApp.userService.infra.exception_handler.exceptions.EmailIsntConfirmed;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EmailAlreadyExistsValidator.class)
    public ResponseEntity<Map<String, String>> EmailAlreadyExists(EmailAlreadyExistsValidator e) {
        var body = ErrorMessage(e.getMessage());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(EmailIsntConfirmed.class)
    public ResponseEntity<Map<String,String>> EmailIsntConfirmed(EmailIsntConfirmed e) {
        var body = ErrorMessage(e.getMessage());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Map<String ,String>> userOrPasswordWrong(AuthenticationException e) {
        var body = ErrorMessage(e.getMessage());
        return ResponseEntity.status(403).body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String ,String>> illegalArgument(IllegalArgumentException e) {
        var body = ErrorMessage(e.getMessage());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String ,String>> entityNotFound(EntityNotFoundException e) {
        var body = ErrorMessage(e.getMessage());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String ,String>> illegalState(IllegalStateException e) {
        var body = ErrorMessage(e.getMessage());
        return ResponseEntity.badRequest().body(body);
    }

    private Map<String,String> ErrorMessage(String errorMessage) {
        Map<String, String> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("error", errorMessage);
        return body;
    }
}
