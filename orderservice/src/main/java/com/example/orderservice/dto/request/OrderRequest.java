package com.example.orderservice.dto.request;

import com.example.orderservice.domain.DeliveryAddress;
import com.example.orderservice.domain.DeliveryMethod;
import com.example.orderservice.domain.OrderType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    @NotNull
    private OrderType orderType;

    @NotNull
    private Long itemId;

    // ONLINE
    private Double deliveryFee;
    private DeliveryMethod deliveryMethod;
    private DeliveryAddress deliveryAddress;
}
