package com.example.userservice.repository;

import com.example.userservice.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT AVG(r.score) FROM Review r WHERE r.ratedUser.id = :userId")
    Double getAverageRating(Long userId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.ratedUser.id = :userId")
    Long countReviews(Long userId);

    List<Review> findByRatedUserId(Long userId);
}
