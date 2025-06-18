package com.example.orderservice.service;

import com.example.common.event.order.OrderAcceptedEvent;
import com.example.orderservice.client.ItemServiceClient;
import com.example.orderservice.domain.*;
import com.example.orderservice.dto.request.OrderRequest;
import com.example.orderservice.dto.response.ItemResponse;
import com.example.orderservice.exception.ItemNotAvailableException;
import com.example.orderservice.exception.OrderNotFound;
import com.example.orderservice.exception.UnauthorizedException;
import com.example.orderservice.kafka.producer.OrderEventPublisher;
import com.example.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ItemServiceClient itemServiceClient;
    private final OrderEventPublisher orderEventPublisher;

    public Order createOrderRequest(OrderRequest request, Long userId) {
        ItemResponse itemResponse = itemServiceClient.getItemById(request.getItemId());

        if (itemResponse.getItemStatus() != ItemStatus.ACTIVE) {
            throw new ItemNotAvailableException("Item is not available");
        }

        Order order = Order.builder()
                .sellerId(itemResponse.getUserId())
                .buyerId(userId)
                .itemId(itemResponse.getId())
                .orderStatus(OrderStatus.PENDING_SELLER_CONFIRMATION)
                .orderType(request.getOrderType())
                .price(itemResponse.getPrice())
                .build();

        return orderRepository.save(order);
    }

    public Order acceptOrder(Long orderId, Long userId) {
        Order order = getOrderById(orderId);
        checkIfSeller(order, userId);
        if (order.getOrderType() == OrderType.PERSONAL) {
            order.setOrderStatus(OrderStatus.AWAITING_PICKUP);
        } else {
            order.setOrderStatus(OrderStatus.AWAITING_PAYMENT);
        }
        orderEventPublisher.publishOrderAccepted(OrderAcceptedEvent.builder()
                .orderId(orderId)
                .itemId(order.getItemId())
                .build());
        return orderRepository.save(order);
    }

    public Order rejectOrder(Long orderId, Long userId) {
        Order order = getOrderById(orderId);
        checkIfSeller(order, userId);
        order.setOrderStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    public Order getOrderIfAuthorized(Long orderId, Long userId) {
        Order order = getOrderById(orderId);
        checkIfSellerOrBuyer(order, userId);
        return order;
    }

    private Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFound(orderId));
    }

    private void checkIfSeller(Order order, Long userId) {
        if (!Objects.equals(order.getSellerId(), userId)) {
            throw new UnauthorizedException("User is not owner of this order");
        }
    }

    private void checkIfSellerOrBuyer(Order order, Long userId) {
        if (!Objects.equals(order.getSellerId(), userId)
                || !Objects.equals(order.getBuyerId(), userId)) {
            throw new UnauthorizedException("User is not owner of this order");
        }
    }
}
