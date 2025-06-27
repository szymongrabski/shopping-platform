package com.example.userservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private LocalDate birthDate;

    @OneToMany(mappedBy = "ratedUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;
}
