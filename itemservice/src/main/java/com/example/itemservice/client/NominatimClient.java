package com.example.itemservice.client;

import com.example.itemservice.dto.response.NominatimResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

// todo implement fallback
@FeignClient(name = "nominatimClient",
        url ="${nominatim.api.url:https://nominatim.openstreetmap.org}")
public interface NominatimClient {
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    List<NominatimResponse> searchCity(
            @RequestParam("q") String cityName,
            @RequestParam(value = "format", defaultValue = "json") String format,
            @RequestParam(value = "limit", defaultValue = "1") int limit
    );
}