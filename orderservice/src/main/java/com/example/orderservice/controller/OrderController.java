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

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id, Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        Order order = orderService.getOrderIfAuthorized(id, userId);
        return ResponseEntity.ok(order);
    }

    @PostMapping
    public ResponseEntity<Order> createOrderRequest(@RequestBody @Valid OrderRequest request,
                                                    Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        Order order = orderService.createOrderRequest(request, userId);

        URI location = URI.create(String.format("/api/orders/%d", order.getId()));
        return ResponseEntity.created(location).body(order);
    }

    @PatchMapping("/{id}/accept")
    public ResponseEntity<Order> acceptOrderRequest(@PathVariable Long id,
                                             Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        Order acceptedOrder = orderService.acceptOrder(id, userId);
        return ResponseEntity.ok(acceptedOrder);
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<Order> rejectOrderRequest(@PathVariable Long id,
                                             Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        Order rejectedOrder = orderService.rejectOrder(id, userId);
        return ResponseEntity.ok(rejectedOrder);
    }

    @PatchMapping("/{id}/confirm-pickup")
    public ResponseEntity<Order> confirmPickup(@PathVariable Long id,
                                               Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        Order order = orderService.confirmPickup(id, userId);
        return ResponseEntity.ok(order);
    }

    @PatchMapping("/{id}/extend-pickup")
    public ResponseEntity<Order> extendPickupDeadline(@PathVariable Long id,
                                                      @RequestParam int extraHours,
                                                      Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        Order updatedOrder = orderService.extendPickupDeadline(id, userId, extraHours);
        return ResponseEntity.ok(updatedOrder);
    }
}
