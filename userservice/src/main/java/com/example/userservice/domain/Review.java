package com.example.userservice.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "rated_user_id", nullable = false)
    private User ratedUser;

    @ManyToOne
    @JoinColumn(name = "rater_user_id", nullable = false)
    private User raterUser;

    @Column(nullable = false)
    private Double score;

    private String comment;

    @Column(nullable = false)
    private Long orderId;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
