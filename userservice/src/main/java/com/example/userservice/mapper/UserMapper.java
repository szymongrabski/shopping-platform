package com.example.userservice.mapper;

import com.example.userservice.domain.User;
import com.example.userservice.dto.response.UserResponse;
import com.example.userservice.dto.response.UserWithRatingResponse;
import com.example.userservice.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMapper {
    private final ReviewService reviewService;

    public UserWithRatingResponse toUserWithRating(User user) {
        Double rating = reviewService.getAverageRating(user.getId());
        Long reviewCount = reviewService.getReviewCount(user.getId());
        return UserWithRatingResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .birthDate(user.getBirthDate())
                .rating(rating)
                .amountOfReviews(reviewCount)
                .build();
    }

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

}
