package com.example.userservice.config;

import com.example.userservice.domain.User;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final UserService userService;

    @Override
    public void run(String... args) {
        User user = User.builder()
                .id(1L)
                .email("jan@example.com")
                .birthDate(LocalDate.now().minusYears(20))
                .firstName("Jan")
                .lastName("Kowalski")
                .phoneNumber("111222333")
                .build();

        User user2 = User.builder()
                .id(2L)
                .email("jan2@example.com")
                .birthDate(LocalDate.now().minusYears(22))
                .firstName("Jan")
                .lastName("Kowal")
                .phoneNumber("333222111")
                .build();

        User user3 = User.builder()
                .id(3L)
                .email("jan3@example.com")
                .birthDate(LocalDate.now().minusYears(24))
                .firstName("Jan")
                .lastName("Kowalik")
                .phoneNumber("222111333")
                .build();

        userService.saveUser(user);
        userService.saveUser(user2);
        userService.saveUser(user3);
    }
}
