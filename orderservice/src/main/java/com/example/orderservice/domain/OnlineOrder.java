package com.example.orderservice.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OnlineOrder extends Order {
    private Double deliveryFee;

    @Enumerated(EnumType.STRING)
    private DeliveryMethod deliveryMethod;

    @Embedded
    private DeliveryAddress deliveryAddress;
}
