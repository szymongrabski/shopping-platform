package com.example.orderservice.exception.conflict;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
