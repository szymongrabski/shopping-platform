package com.example.orderservice.domain;

public enum OrderStatus {
    PENDING_SELLER_CONFIRMATION,
    AWAITING_PAYMENT,
    AWAITING_PICKUP,
    PICKUP_EXPIRED,
    COMPLETED,
    CANCELLED
}