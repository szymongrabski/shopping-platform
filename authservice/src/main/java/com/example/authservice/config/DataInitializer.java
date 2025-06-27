package com.example.authservice.config;

import com.example.authservice.domain.User;
import com.example.authservice.dto.request.RegisterRequest;
import com.example.authservice.service.UserService;
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
        RegisterRequest request1 = RegisterRequest.builder()
                .email("jan@example.com")
                .password("password123P!")
                .birthDate(LocalDate.now().minusYears(20))
                .firstName("Jan")
                .lastName("Kowalski")
                .phoneNumber("111222333")
                .build();

        RegisterRequest request2 = RegisterRequest.builder()
                .email("jan2@example.com")
                .password("password123P!")
                .birthDate(LocalDate.now().minusYears(22))
                .firstName("Jan")
                .lastName("Kowal")
                .phoneNumber("333222111")
                .build();

        RegisterRequest request3 = RegisterRequest.builder()
                .email("jan3@example.com")
                .password("password123P!")
                .birthDate(LocalDate.now().minusYears(22))
                .firstName("Jan")
                .lastName("Kowalik")
                .phoneNumber("222111333")
                .build();

        User user = userService.createUser(request1);
        User user2 = userService.createUser(request2);
        User user3 = userService.createUser(request3);
        userService.saveUser(user);
        userService.saveUser(user2);
        userService.saveUser(user3);
    }
}
