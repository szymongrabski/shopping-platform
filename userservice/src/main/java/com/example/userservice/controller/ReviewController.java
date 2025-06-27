package com.example.userservice.controller;

import com.example.userservice.domain.Review;
import com.example.userservice.dto.request.ReviewRequest;
import com.example.userservice.dto.response.ReviewResponse;
import com.example.userservice.mapper.ReviewMapper;
import com.example.userservice.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;

    @PostMapping
    public ResponseEntity<ReviewResponse> addReview(@RequestBody @Valid ReviewRequest reviewRequest,
                                                    Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        Review review = reviewService.addReview(reviewRequest, userId);

        URI location = URI.create("/api/reviews/" + review.getId());

        return ResponseEntity.created(location).body(reviewMapper.toResponse(review));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ReviewResponse> deleteReview(@PathVariable Long id,
                                                       Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        reviewService.deleteReview(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> getReview(@PathVariable Long id) {
        Review review = reviewService.getReviewById(id);
        return ResponseEntity.ok(reviewMapper.toResponse(review));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewResponse>> getReviewByUserId(@PathVariable Long userId) {
        List<Review> reviews = reviewService.getReviewsForUser(userId);
        return ResponseEntity.ok(reviewMapper.toResponseList(reviews));
    }
}
