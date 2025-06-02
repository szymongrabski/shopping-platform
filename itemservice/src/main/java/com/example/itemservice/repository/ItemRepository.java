package com.example.itemservice.repository;

import com.example.itemservice.domain.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @EntityGraph(attributePaths = {"city", "imageUrls"})
    Page<Item> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"city", "imageUrls"})
    Optional<Item> findById(Long id);
}
