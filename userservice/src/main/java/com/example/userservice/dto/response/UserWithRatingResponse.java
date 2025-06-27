package com.example.userservice.dto.response;

import lombok.*;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserWithRatingResponse {
    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private LocalDate birthDate;

    private Double rating;

    private Long amountOfReviews;
}
