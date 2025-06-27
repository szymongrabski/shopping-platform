package com.example.orderservice.repository;

import com.example.orderservice.domain.Order;
import com.example.orderservice.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByOrderStatusAndPickupDeadlineBefore(OrderStatus status, LocalDateTime before);
}
