package com.example.orderservice.exception.notfound;

public class OrderNotFound extends NotFoundException {
    public OrderNotFound(Long id) {
        super("Order with id " + id + " not found");
    }
}
