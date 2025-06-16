package com.example.orderservice.domain;

public enum OrderStatus {
    PENDING_SELLER_CONFIRMATION,
    CONFIRMED,
    REJECTED,
    PAID,
    SHIPPED,
    CANCELLED
}
