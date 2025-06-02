package com.example.itemservice.service;

import com.example.itemservice.domain.City;
import com.example.itemservice.domain.Item;
import com.example.itemservice.domain.ItemStatus;
import com.example.itemservice.dto.request.ItemRequest;
import com.example.itemservice.exceptions.notfound.ItemNotFoundException;
import com.example.itemservice.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final CityService cityService;
    private final ItemRepository itemRepository;

    public Page<Item> findAll(Pageable pageable) {
        return itemRepository.findAll(pageable);
    }

    public Item findById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));
    }

    public void deleteById(Long id) {
        itemRepository.deleteById(id);
    }

    public Item createItem(ItemRequest itemRequest, Long userId) {
        City city = cityService.findCityByName(itemRequest.getCityName());

        Item item = Item.builder()
                .title(itemRequest.getTitle())
                .userId(userId)
                .description(itemRequest.getDescription())
                .category(itemRequest.getCategory())
                .price(itemRequest.getPrice())
                .imageUrls(itemRequest.getImageUrls())
                .itemStatus(ItemStatus.ACTIVE)
                .city(city)
                .build();
        return itemRepository.save(item);
    }

    public Item updateItem(Long id, ItemRequest itemRequest) {
        City city = cityService.findCityByName(itemRequest.getCityName());

        Item item = Item.builder()
                .id(id)
                .title(itemRequest.getTitle())
                .description(itemRequest.getDescription())
                .category(itemRequest.getCategory())
                .price(itemRequest.getPrice())
                .imageUrls(itemRequest.getImageUrls())
                .city(city)
                .build();
        return itemRepository.save(item);
    }
}
