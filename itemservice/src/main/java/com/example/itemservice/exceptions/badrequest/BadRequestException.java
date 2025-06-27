package com.example.itemservice.exceptions.badrequest;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
