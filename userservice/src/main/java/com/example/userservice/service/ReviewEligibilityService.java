package com.example.userservice.service;

import com.example.userservice.domain.ReviewEligibility;
import com.example.userservice.exceptions.notfound.ReviewEligibilityNotFound;
import com.example.userservice.repository.ReviewEligibilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewEligibilityService {
    private final ReviewEligibilityRepository reviewEligibilityRepository;

    public ReviewEligibility addReviewEligibility(Long orderId, Long reviewerId, Long revieweeId) {
        ReviewEligibility reviewEligibility = ReviewEligibility.builder()
                .orderId(orderId)
                .reviewerId(reviewerId)
                .revieweeId(revieweeId)
                .build();
        return reviewEligibilityRepository.save(reviewEligibility);
    }

    public ReviewEligibility getReviewEligibility(Long orderId, Long reviewerId, Long revieweeId) {
        return reviewEligibilityRepository.findByOrderIdAndReviewerIdAndRevieweeId(orderId, reviewerId, revieweeId)
                .orElseThrow(() -> new ReviewEligibilityNotFound(reviewerId, revieweeId));
    }

    public void deleteReviewEligibility(ReviewEligibility reviewEligibility) {
        reviewEligibilityRepository.delete(reviewEligibility);
    }
}
