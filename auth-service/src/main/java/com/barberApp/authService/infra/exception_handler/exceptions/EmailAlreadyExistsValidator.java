package com.barberApp.authService.infra.exception_handler.exceptions;

public class EmailAlreadyExistsValidator extends RuntimeException{
    public EmailAlreadyExistsValidator(String message) {
        super(message);
    }
}
