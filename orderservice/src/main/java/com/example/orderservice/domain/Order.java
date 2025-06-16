package com.example.orderservice.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "orders")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long itemId;
    private Long buyerId;
    private Long sellerId;

    private Double price;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
