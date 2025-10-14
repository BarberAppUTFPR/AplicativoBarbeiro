package com.barberApp.userService.infra.exception_handler.exceptions;

public class EmailAlreadyExistsValidator extends RuntimeException{
    public EmailAlreadyExistsValidator(String message) {
        super(message);
    }
}
