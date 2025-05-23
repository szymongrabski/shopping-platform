package com.example.common.kafka;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum KafkaTopic {
    USER_REGISTERED("user-registered");

    private final String topicName;
}
