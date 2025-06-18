package com.example.authservice.kafka.consumer;

import com.example.authservice.service.UserService;
import com.example.common.event.user.UserChangedEmailEvent;
import com.example.common.event.user.UserDeletedEvent;
import com.example.common.kafka.KafkaTopic;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@KafkaListener(
        topics = KafkaTopic.USER_EVENTS,
        groupId = "auth-service-group",
        containerFactory = "kafkaListenerContainerFactory")
public class UserEventListener {
    private final UserService userService;

    @KafkaHandler
    public void handleUserDeleted(UserDeletedEvent event) {
        userService.deleteUserById(event.getUserId());
    }

    @KafkaHandler
    public void handleUserChangedEmail(UserChangedEmailEvent event) {
        userService.changeEmail(event.getUserId(), event.getEmail());
    }

    @KafkaHandler(isDefault = true)
    public void unknown(Object object) {
        System.out.println("Unkown type received: " + object);
    }
}
