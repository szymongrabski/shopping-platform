package com.example.common.event.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisteredEvent implements Serializable {
    private Long id;

    private String email;

    private String username;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private LocalDate birthDate;
}
