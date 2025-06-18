package com.example.orderservice.exception;

public class OrderNotFound extends RuntimeException {
    public OrderNotFound(Long id) {
        super("Order with id " + id + " not found");
    }
}
