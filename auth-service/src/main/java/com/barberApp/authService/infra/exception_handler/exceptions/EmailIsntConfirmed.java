package com.barberApp.authService.infra.exception_handler.exceptions;

public class EmailIsntConfirmed extends RuntimeException{
    public EmailIsntConfirmed (String message) {
        super(message);
    }
}
