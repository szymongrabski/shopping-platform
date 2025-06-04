package com.example.itemservice.specification;


import com.example.itemservice.domain.Category;
import com.example.itemservice.domain.Item;
import com.example.itemservice.domain.ItemStatus;
import org.springframework.data.jpa.domain.Specification;

public class ItemSpecification {
    public static Specification<Item> titleContains(String title) {
        return (root, query, cb) -> title == null ? null :
                cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Item> priceGreaterThanOrEqual(Double minPrice) {
        return (root, query, cb) -> minPrice == null ? null :
                cb.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Item> priceLessThanOrEqual(Double maxPrice) {
        return (root, query, cb) -> maxPrice == null ? null :
                cb.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    public static Specification<Item> hasCategory(Category category) {
        return (root, query, cb) -> category == null ? null :
                cb.equal(root.get("category"), category);
    }

    public static Specification<Item> hasStatus(ItemStatus status) {
        return (root, query, cb) -> status == null ? null :
                cb.equal(root.get("itemStatus"), status);
    }

    public static Specification<Item> withinBoundingBox(double minLat, double maxLat, double minLon, double maxLon) {
        return (root, query, cb) -> cb.and(
                cb.between(root.get("city").get("latitude"), minLat, maxLat),
                cb.between(root.get("city").get("longitude"), minLon, maxLon)
        );
    }
}
