package com.example.orderservice.controller;

import com.example.orderservice.domain.Order;
import com.example.orderservice.dto.request.OrderRequest;
import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrderRequest(@RequestBody OrderRequest request,
                                             Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        Order order = orderService.createOrderRequest(request, userId);

        URI location = URI.create(String.format("/api/orders/%d", order.getId()));
        return ResponseEntity.created(location).body(order);
    }
}
