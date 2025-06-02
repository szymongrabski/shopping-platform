package com.example.userservice.kafka.consumer;

import com.example.common.event.UserRegisteredEvent;
import com.example.common.kafka.KafkaTopic;
import com.example.userservice.domain.User;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@KafkaListener(
        topics = KafkaTopic.USER_EVENTS,
        groupId = "user-service-group",
        containerFactory = "kafkaListenerContainerFactory"
)
public class UserEventListener {
    private final UserService userService;

    @KafkaHandler
    public void handleUserRegistered(UserRegisteredEvent event) {
        User user = User.builder()
                .id(event.getId())
                .email(event.getEmail())
                .username(event.getUsername())
                .firstName(event.getFirstName())
                .lastName(event.getLastName())
                .birthDate(event.getBirthDate())
                .build();
        userService.saveUser(user);
    }

    @KafkaHandler(isDefault = true)
    public void unknown(Object object) {
        System.out.println("Unkown type received: " + object);
    }
}
