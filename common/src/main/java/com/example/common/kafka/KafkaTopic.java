package com.example.common.kafka;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum KafkaTopic {
    USER_EVENTS("user-events");

    private final String topicName;
}
