package com.example.itemservice.service;

import com.example.itemservice.domain.Category;
import com.example.itemservice.domain.City;
import com.example.itemservice.domain.Item;
import com.example.itemservice.domain.ItemStatus;
import com.example.itemservice.dto.request.ItemRequest;
import com.example.itemservice.dto.request.LocationFilterRequest;
import com.example.itemservice.exceptions.badrequest.BadRequestException;
import com.example.itemservice.exceptions.conflict.InvalidItemStatusTransition;
import com.example.itemservice.exceptions.notfound.ItemNotFoundException;
import com.example.itemservice.repository.ItemRepository;
import com.example.itemservice.specification.ItemSpecification;
import com.example.itemservice.util.GeoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final CityService cityService;
    private final ItemRepository itemRepository;
    private static final EnumMap<ItemStatus, Set<ItemStatus>> allowedStatusTransitions = new EnumMap<>(ItemStatus.class);

    static {
        allowedStatusTransitions.put(ItemStatus.ACTIVE, EnumSet.of(ItemStatus.ORDERED, ItemStatus.ARCHIVED));
        allowedStatusTransitions.put(ItemStatus.ORDERED, EnumSet.of(ItemStatus.SOLD, ItemStatus.ACTIVE));
        allowedStatusTransitions.put(ItemStatus.SOLD, EnumSet.noneOf(ItemStatus.class));
        allowedStatusTransitions.put(ItemStatus.ARCHIVED, EnumSet.of(ItemStatus.ACTIVE));
    }

    public Page<Item> findAll(Pageable pageable,
                              String title,
                              Double minPrice,
                              Double maxPrice,
                              Category category,
                              ItemStatus status,
                              LocationFilterRequest locationFilter) {

        Specification<Item> spec = ItemSpecification.titleContains(title)
                .and(ItemSpecification.priceGreaterThanOrEqual(minPrice))
                .and(ItemSpecification.priceLessThanOrEqual(maxPrice))
                .and(ItemSpecification.hasCategory(category))
                .and(ItemSpecification.hasStatus(status));

        if (locationFilter != null) {
            if ((locationFilter.getCityName() == null) != (locationFilter.getRadiusKm() == null)) {
                throw new BadRequestException("You must provide both cityName and radiusKm, or neither of them");
            }
            if (locationFilter.getCityName() != null) {
                City city = cityService.findCityByName(locationFilter.getCityName());
                double[] box = GeoUtils.calculateBoundingBox(city.getLatitude(), city.getLongitude(), locationFilter.getRadiusKm());
                spec = spec.and(ItemSpecification.withinBoundingBox(box[0], box[1], box[2], box[3]));
            }
        }
        return itemRepository.findAll(spec, pageable);
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

    public Item changeStatus(Long id, ItemStatus newStatus) {
        Item item = findById(id);
        if (!canTransition(item.getItemStatus(), newStatus)) {
            throw new InvalidItemStatusTransition(item.getItemStatus(), newStatus);
        }
        item.setItemStatus(newStatus);
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

    private static boolean canTransition(ItemStatus from, ItemStatus to) {
        return allowedStatusTransitions.getOrDefault(from, EnumSet.noneOf(ItemStatus.class)).contains(to);
    }
}
