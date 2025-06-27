package com.example.userservice.service;

import com.example.userservice.domain.Review;
import com.example.userservice.domain.ReviewEligibility;
import com.example.userservice.domain.User;
import com.example.userservice.dto.request.ReviewRequest;
import com.example.userservice.exceptions.notfound.ReviewNotFound;
import com.example.userservice.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final UserService userService;
    private final ReviewRepository reviewRepository;
    private final ReviewEligibilityService reviewEligibilityService;

    @Transactional
    public Review addReview(ReviewRequest reviewRequest, Long reviewerId) {
        ReviewEligibility reviewEligibility = reviewEligibilityService.getReviewEligibility(
                reviewRequest.getOrderId(),
                reviewerId,
                reviewRequest.getUserId()
        );

        User reviewer = userService.getUserById(reviewerId);
        User reviewee = userService.getUserById(reviewRequest.getUserId());

        Review review = Review.builder()
                .comment(reviewRequest.getComment())
                .score(reviewRequest.getScore())
                .orderId(reviewRequest.getOrderId())
                .ratedUser(reviewee)
                .raterUser(reviewer)
                .build();

        reviewEligibilityService.deleteReviewEligibility(reviewEligibility);
        return reviewRepository.save(review);
    }

    public Review getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFound(reviewId));
    }

    public void deleteReview(Long reviewId, Long userId) {
        Review review = getReviewById(reviewId);
        userService.authorizeUser(review.getRaterUser().getId(), userId);
        reviewRepository.delete(review);
    }

    public List<Review> getReviewsForUser(Long userId) {
        return reviewRepository.findByRatedUserId(userId);
    }

    public Double getAverageRating(Long userId) {
        Double rating = reviewRepository.getAverageRating(userId);
        return rating == null ? 0 : rating;
    }

    public Long getReviewCount(Long userId) {
        Long count = reviewRepository.countReviews(userId);
        return count == null ? 0 : count;
    }
}
