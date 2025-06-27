package com.example.orderservice.domain;

public enum OrderStatus {
    PENDING_SELLER_CONFIRMATION,
    AWAITING_PICKUP,
    DISPUTE,
    COMPLETED,
    CANCELLED
}