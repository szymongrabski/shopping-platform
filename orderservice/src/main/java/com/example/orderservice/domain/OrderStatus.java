package com.example.orderservice.domain;

public enum OrderStatus {
    PENDING_SELLER_CONFIRMATION,
    AWAITING_PICKUP,
    REJECTED,
    DISPUTE,
    COMPLETED,
    CANCELLED
}