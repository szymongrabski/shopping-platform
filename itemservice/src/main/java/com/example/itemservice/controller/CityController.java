package com.example.itemservice.controller;

import com.example.itemservice.domain.City;
import com.example.itemservice.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityController {
    private final CityService cityService;

    @GetMapping("/{name}")
    public ResponseEntity<City> getCity(@PathVariable String name) {
        return ResponseEntity.ok(cityService.findCityByName(name));
    }
}
