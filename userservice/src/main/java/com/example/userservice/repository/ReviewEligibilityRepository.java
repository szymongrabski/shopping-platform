package com.example.userservice.repository;

import com.example.userservice.domain.ReviewEligibility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ReviewEligibilityRepository extends JpaRepository<ReviewEligibility, Long> {
    Optional<ReviewEligibility> findByOrderIdAndReviewerIdAndRevieweeId(Long orderId, Long reviewerId, Long revieweeId);
}
