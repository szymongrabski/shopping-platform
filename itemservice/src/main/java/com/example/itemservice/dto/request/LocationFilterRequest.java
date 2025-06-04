package com.example.itemservice.dto.request;

import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LocationFilterRequest {
    private String cityName;
    private Double radiusKm;
}
