package com.example.orderservice.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryAddress {
    private String street;
    private String city;
    private String postalCode;
    private String additionalInfo;
}