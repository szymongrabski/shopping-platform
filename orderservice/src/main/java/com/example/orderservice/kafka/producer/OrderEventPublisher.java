package com.example.orderservice.kafka.producer;

import com.example.common.event.order.OrderAcceptedEvent;
import com.example.common.kafka.KafkaTopic;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishOrderAccepted(OrderAcceptedEvent event) {
        kafkaTemplate.send(KafkaTopic.ORDER_EVENTS, event);
    }
}
