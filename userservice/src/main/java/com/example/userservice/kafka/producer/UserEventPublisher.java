package com.example.userservice.kafka.producer;

import com.example.common.event.user.UserChangedEmailEvent;
import com.example.common.event.user.UserDeletedEvent;
import com.example.common.kafka.KafkaTopic;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishUserDeleted(UserDeletedEvent event) {
        kafkaTemplate.send(KafkaTopic.USER_EVENTS, event);
    }

    public void publishUserEmailChanged(UserChangedEmailEvent event) {
        kafkaTemplate.send(KafkaTopic.USER_EVENTS, event);
    }
}
