package com.example.userservice.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "review_eligibility",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"orderId", "reviewerId", "revieweeId"})
        }
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReviewEligibility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;
    private Long reviewerId;
    private Long revieweeId;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
