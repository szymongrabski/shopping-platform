package com.example.authservice.kafka.producer;

import com.example.common.event.UserRegisteredEvent;
import com.example.common.kafka.KafkaTopic;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishUserRegistered(UserRegisteredEvent event) {
        kafkaTemplate.send(KafkaTopic.USER_EVENTS.getTopicName(), event);
    }
}