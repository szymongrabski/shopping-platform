package com.example.orderservice.exception.conflict;

import com.example.orderservice.domain.OrderStatus;

public class InvalidOrderStatus extends ConflictException {
    public InvalidOrderStatus(Long id, OrderStatus status) {
        super("Order with id " + id + " has status " + status.name());
    }
}
