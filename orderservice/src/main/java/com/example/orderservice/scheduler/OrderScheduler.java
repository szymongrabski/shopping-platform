package com.example.orderservice.scheduler;

import com.example.common.event.order.OrderCompletedEvent;
import com.example.orderservice.domain.Order;
import com.example.orderservice.domain.OrderStatus;
import com.example.orderservice.kafka.producer.OrderEventPublisher;
import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderScheduler {
    private final OrderService orderService;
    private final OrderEventPublisher orderEventPublisher;

    @Scheduled(fixedRate = 60)
    public void completeExpiredOrders() {
        List<Order> expiredOrders = orderService.getExpiredOrders();
        for (Order order : expiredOrders) {
            order.setOrderStatus(OrderStatus.COMPLETED);
            orderEventPublisher.publishOrderCompletedEvent(OrderCompletedEvent.builder()
                    .orderId(order.getId())
                    .itemId(order.getItemId())
                    .buyerId(order.getBuyerId())
                    .sellerId(order.getSellerId())
                    .build());
        }
        orderService.saveAllOrders(expiredOrders);
    }
}
