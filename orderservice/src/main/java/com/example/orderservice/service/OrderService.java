package com.example.orderservice.service;

import com.example.common.event.order.CancellationType;
import com.example.common.event.order.OrderAcceptedEvent;
import com.example.common.event.order.OrderCancelledEvent;
import com.example.common.event.order.OrderCompletedEvent;
import com.example.orderservice.client.ItemServiceClient;
import com.example.orderservice.domain.*;
import com.example.orderservice.dto.request.OrderRequest;
import com.example.orderservice.dto.response.ItemResponse;
import com.example.orderservice.exception.badrequest.BadRequestException;
import com.example.orderservice.exception.conflict.ConflictException;
import com.example.orderservice.exception.conflict.InvalidOrderStatus;
import com.example.orderservice.exception.conflict.ItemNotAvailableException;
import com.example.orderservice.exception.forbidden.ForbiddenException;
import com.example.orderservice.exception.notfound.OrderNotFound;
import com.example.orderservice.kafka.producer.OrderEventPublisher;
import com.example.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final static int PICKUP_DEADLINE_HOURS = 72;
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
                .price(itemResponse.getPrice())
                .build();

        return orderRepository.save(order);
    }

    public Order acceptOrder(Long orderId, Long userId) {
        Order order = getOrderById(orderId);
        checkIfSeller(order, userId);
        order.setOrderStatus(OrderStatus.AWAITING_PICKUP);
        order.setPickupDeadline(LocalDateTime.now().plusMinutes(PICKUP_DEADLINE_HOURS));
        orderEventPublisher.publishOrderAccepted(OrderAcceptedEvent.builder()
                .orderId(orderId)
                .itemId(order.getItemId())
                .build());
        return orderRepository.save(order);
    }

    public Order rejectOrder(Long orderId, Long userId) {
        Order order = getOrderById(orderId);
        checkIfSeller(order, userId);
        order.setOrderStatus(OrderStatus.REJECTED);
        return orderRepository.save(order);
    }

    public Order cancelOrder(Long orderId, Long userId) {
        Order order = getOrderById(orderId);
        checkIfSellerOrBuyer(order, userId);

        if (order.getOrderStatus() != OrderStatus.AWAITING_PICKUP) {
            throw new InvalidOrderStatus(orderId, order.getOrderStatus());
        }
        order.setOrderStatus(OrderStatus.CANCELLED);

        CancellationType cancellationType = Objects.equals(userId, order.getSellerId())
                ? CancellationType.SELLER_CANCEL : CancellationType.BUYER_CANCEL;
        orderEventPublisher.publishOrderCancelledEvent(OrderCancelledEvent.builder()
                .orderId(orderId)
                .itemId(order.getItemId())
                .buyerId(order.getBuyerId())
                .sellerId(order.getSellerId())
                .cancellationType(cancellationType)
                .build());

        return orderRepository.save(order);
    }

    public Order getOrderIfAuthorized(Long orderId, Long userId) {
        Order order = getOrderById(orderId);
        checkIfSellerOrBuyer(order, userId);
        return order;
    }

    public List<Order> getExpiredOrders() {
        LocalDateTime now = LocalDateTime.now();
        return orderRepository
                .findByOrderStatusAndPickupDeadlineBefore(OrderStatus.AWAITING_PICKUP, now);
    }

    public Order confirmPickup(Long orderId, Long userId) {
        Order order = getOrderById(orderId);
        checkIfBuyer(order, userId);
        order.setOrderStatus(OrderStatus.COMPLETED);
        orderEventPublisher.publishOrderCompletedEvent(OrderCompletedEvent.builder()
                .orderId(orderId)
                .itemId(order.getItemId())
                .sellerId(order.getSellerId())
                .buyerId(order.getBuyerId())
                .build());
        return orderRepository.save(order);
    }

    public Order extendPickupDeadline(Long orderId, Long userId, int extraHours) {
        Order order = getOrderById(orderId);
        checkIfSeller(order, userId);

        if (extraHours <= 0 || extraHours > 72) {
            throw new BadRequestException("extraHours must be between 1 and 72");
        }
        if (order.getOrderStatus() != OrderStatus.AWAITING_PICKUP) {
            throw new ConflictException("Order is not in AWAITING_PICKUP status");
        }

        order.setPickupDeadline(order.getPickupDeadline().plusHours(extraHours));
        return orderRepository.save(order);
    }

    public void saveAllOrders(List<Order> orders) {
        orderRepository.saveAll(orders);
    }

    private Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFound(orderId));
    }

    private void checkIfSeller(Order order, Long userId) {
        if (!Objects.equals(order.getSellerId(), userId)) {
            throw new ForbiddenException("User is not owner of this order");
        }
    }

    private void checkIfBuyer(Order order, Long userId) {
        if (!Objects.equals(order.getBuyerId(), userId)) {
            throw new ForbiddenException("User is not owner of this order");
        }
    }

    private void checkIfSellerOrBuyer(Order order, Long userId) {
        if (!Objects.equals(order.getSellerId(), userId) &&
                !Objects.equals(order.getBuyerId(), userId)) {
            throw new ForbiddenException("User is not owner of this order");
        }
    }
}
