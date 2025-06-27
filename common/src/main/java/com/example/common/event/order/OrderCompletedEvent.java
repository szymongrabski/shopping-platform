package com.example.common.event.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCompletedEvent {
    private Long orderId;
    private Long itemId;
    private Long sellerId;
    private Long buyerId;
}
