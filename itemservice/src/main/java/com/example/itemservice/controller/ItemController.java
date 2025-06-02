package com.example.itemservice.controller;

import com.example.itemservice.domain.Item;
import com.example.itemservice.dto.request.ItemRequest;
import com.example.itemservice.exceptions.forbidden.ForbiddenException;
import com.example.itemservice.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<Page<Item>> getItems(Pageable pageable) {
        return ResponseEntity.ok(itemService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItem(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody @Valid ItemRequest request,
                                           Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        Item item = itemService.createItem(request, userId);
        URI location = URI.create("/api/items/" + item.getId());
        return ResponseEntity.created(location).body(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Long id,
                                           @RequestBody @Valid ItemRequest request,
                                           Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        Item item = itemService.findById(id);
        if (!userId.equals(item.getUserId())) {
            throw new ForbiddenException("You do not have permission to update this item");
        }

        return ResponseEntity.ok(itemService.updateItem(id, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
