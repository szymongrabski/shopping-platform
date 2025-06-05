package com.example.itemservice.exceptions.conflict;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
