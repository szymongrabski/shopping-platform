package com.example.orderservice.controller;

import com.example.orderservice.domain.Order;
import com.example.orderservice.dto.request.OrderRequest;
import com.example.orderservice.service.OrderService;
import jakarta.validation.Valid;
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
    public ResponseEntity<Order> createOrderRequest(@RequestBody @Valid OrderRequest request,
                                             Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        Order order = orderService.createOrderRequest(request, userId);

        URI location = URI.create(String.format("/api/orders/%d", order.getId()));
        return ResponseEntity.created(location).body(order);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id, Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        Order order = orderService.getOrderIfAuthorized(id, userId);
        return ResponseEntity.ok(order);
    }

    @PatchMapping("/{id}/accept")
    public ResponseEntity<Order> acceptOrder(@PathVariable Long id,
                                             Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        Order acceptedOrder = orderService.acceptOrder(id, userId);
        return ResponseEntity.ok(acceptedOrder);
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<Order> rejectOrder(@PathVariable Long id,
                                             Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        Order rejectedOrder = orderService.rejectOrder(id, userId);
        return ResponseEntity.ok(rejectedOrder);
    }
}
