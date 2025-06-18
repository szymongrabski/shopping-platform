package com.example.orderservice.dto.request;

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
    private Long itemId;

    @NotNull
    private OrderType orderType;
}
