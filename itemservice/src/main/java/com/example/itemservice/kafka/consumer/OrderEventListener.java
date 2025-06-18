package com.example.itemservice.kafka.consumer;


import com.example.common.event.order.OrderAcceptedEvent;
import com.example.common.kafka.KafkaTopic;
import com.example.itemservice.domain.ItemStatus;
import com.example.itemservice.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@KafkaListener(
        topics = KafkaTopic.ORDER_EVENTS,
        groupId = "item-service-group",
        containerFactory = "kafkaListenerContainerFactory"
)
public class OrderEventListener {
    private final ItemService itemService;

    @KafkaHandler
    public void handleOrderAccepted(OrderAcceptedEvent event) {
        System.out.println("Order accepted: " + event);
        itemService.changeStatus(event.getItemId(), ItemStatus.ORDERED);
    }

    @KafkaHandler(isDefault = true)
    public void unknown(Object object) {
        System.out.println("Unkown type received: " + object);
    }
}
