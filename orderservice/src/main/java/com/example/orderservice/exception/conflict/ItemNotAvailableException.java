package com.example.orderservice.exception.conflict;

public class ItemNotAvailableException extends ConflictException {
    public ItemNotAvailableException(String message) {
        super(message);
    }
}