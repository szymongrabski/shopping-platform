package com.example.userservice.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReviewResponse {
    private Long id;
    private Long orderId;
    private String comment;
    private Double score;
    private Long ratedUserId;
    private UserResponse raterUser;
    private LocalDateTime createdAt;
}
