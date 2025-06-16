package com.example.orderservice.client;

import com.example.orderservice.dto.response.ItemResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "itemservice",
        url ="${itemservice.api.url}")
public interface ItemServiceClient {
    @GetMapping("/api/items/{id}")
    ItemResponse getItemById(@PathVariable("id") Long itemId);
}
