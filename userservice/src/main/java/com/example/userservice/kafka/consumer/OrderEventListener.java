package com.example.userservice.kafka.consumer;

import com.example.common.event.order.CancellationType;
import com.example.common.event.order.OrderCancelledEvent;
import com.example.common.event.order.OrderCompletedEvent;
import com.example.common.kafka.KafkaTopic;
import com.example.userservice.service.ReviewEligibilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@KafkaListener(
        topics = KafkaTopic.ORDER_EVENTS,
        groupId = "user-service-group",
        containerFactory = "kafkaListenerContainerFactory"
)
public class OrderEventListener {
    private final ReviewEligibilityService reviewEligibilityService;

    @KafkaHandler
    public void handleOrderCompletedEvent(OrderCompletedEvent event) {
        reviewEligibilityService.addReviewEligibility(event.getOrderId(),
                event.getBuyerId(), event.getSellerId());
    }

    @KafkaHandler
    public void handleOrderCancelledEvent(OrderCancelledEvent event) {
        if (event.getCancellationType() == CancellationType.SELLER_CANCEL) {
            reviewEligibilityService.addReviewEligibility(event.getOrderId(),
                    event.getBuyerId(),
                    event.getSellerId());
        }
    }

    @KafkaHandler(isDefault = true)
    public void unknown(Object object) {
        System.out.println("Unkown type received: " + object);
    }
}
