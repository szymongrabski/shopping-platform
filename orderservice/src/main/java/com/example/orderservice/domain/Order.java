package com.example.orderservice.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long itemId;
    private Long buyerId;
    private Long sellerId;
    private Double price;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime pickupDeadline;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
