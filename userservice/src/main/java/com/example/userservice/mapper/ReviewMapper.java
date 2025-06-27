package com.example.userservice.mapper;

import com.example.userservice.domain.Review;
import com.example.userservice.dto.response.ReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewMapper {
    private final UserMapper userMapper;

    public ReviewResponse toResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .score(review.getScore())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .orderId(review.getOrderId())
                .ratedUserId(review.getRatedUser().getId())
                .raterUser(userMapper.toResponse(review.getRaterUser()))
                .build();
    }

    public List<ReviewResponse> toResponseList(List<Review> reviews) {
        return reviews.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
