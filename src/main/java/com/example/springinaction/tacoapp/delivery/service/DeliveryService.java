package com.example.springinaction.tacoapp.delivery.service;

import com.example.springinaction.tacoapp.order.service.OrderService;
import com.example.springinaction.tacoapp.core.domain.entity.taco.OrderStatus;
import com.example.springinaction.tacoapp.core.domain.entity.taco.TacoOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryService {

    private final OrderService orderService;

    public List<TacoOrder> getOrdersReadyForDelivery() {
        return orderService.getOrdersWithStatus(OrderStatus.READY);
    }

    public List<TacoOrder> getOrdersInDelivery() {
        return orderService.getOrdersWithStatus(OrderStatus.DELIVERING);
    }

    public void takeOrder(Long orderId) {
        orderService.changeTacoOrderStatus(orderId, OrderStatus.DELIVERING);
    }

    public void completeDelivery(Long orderId) {
        orderService.changeTacoOrderStatus(orderId, OrderStatus.DELIVERED);
    }
}
