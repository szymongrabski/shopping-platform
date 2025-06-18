package com.example.orderservice.dto.response;

import com.example.orderservice.domain.ItemStatus;
import lombok.Getter;

@Getter
public class ItemResponse {
    private Long id;
    private Long userId;
    private double price;
    private ItemStatus itemStatus;
}
