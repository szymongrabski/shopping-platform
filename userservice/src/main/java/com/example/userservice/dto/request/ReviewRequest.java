package com.example.userservice.dto.request;

import com.example.userservice.validation.ValidScore;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ReviewRequest {
    @NotNull
    @ValidScore
    private Double score;

    @NotBlank
    @Size(min = 10, max = 50)
    private String comment;

    @NotNull
    private Long userId;

    @NotNull
    private Long orderId;
}
