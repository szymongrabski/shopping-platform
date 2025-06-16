package com.example.orderservice.service;

import com.example.orderservice.client.ItemServiceClient;
import com.example.orderservice.domain.*;
import com.example.orderservice.dto.request.OrderRequest;
import com.example.orderservice.dto.response.ItemResponse;
import com.example.orderservice.exception.ItemNotAvailableException;
import com.example.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ItemServiceClient itemServiceClient;

    public Order createOrderRequest(OrderRequest request, Long userId) {
        ItemResponse itemResponse = itemServiceClient.getItemById(request.getItemId());

        Order order;
        switch (request.getOrderType()) {
            case ONLINE -> {
                OnlineOrder onlineOrder = OnlineOrder.builder()
                        .itemId(request.getItemId())
                        .buyerId(userId)
                        .sellerId(itemResponse.getUserId())
                        .orderStatus(OrderStatus.PENDING_SELLER_CONFIRMATION)
                        .price(itemResponse.getPrice())
                        .deliveryFee(request.getDeliveryFee())
                        .deliveryMethod(request.getDeliveryMethod())
                        .deliveryAddress(request.getDeliveryAddress())
                        .build();

                order = orderRepository.save(onlineOrder);
            }
            case PERSONAL_PICKUP -> {
                PersonalPickupOrder pickupOrder = PersonalPickupOrder.builder()
                        .itemId(request.getItemId())
                        .sellerId(itemResponse.getUserId())
                        .buyerId(userId)
                        .price(itemResponse.getPrice())
                        .orderStatus(OrderStatus.PENDING_SELLER_CONFIRMATION)
                        .build();

                order = orderRepository.save(pickupOrder);
            }
            default -> throw new IllegalArgumentException("Unsupported order type: " + request.getOrderType());
        }
        return order;
    }
}
