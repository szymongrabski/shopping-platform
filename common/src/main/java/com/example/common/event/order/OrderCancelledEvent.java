package com.example.common.event.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCancelledEvent {
    private Long orderId;
    private Long itemId;
    private Long buyerId;
    private Long sellerId;
    private CancellationType cancellationType;
}
