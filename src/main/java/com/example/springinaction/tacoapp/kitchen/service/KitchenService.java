package com.example.springinaction.tacoapp.kitchen.service;

import com.example.springinaction.tacoapp.core.domain.entity.taco.OrderStatus;
import com.example.springinaction.tacoapp.core.domain.entity.taco.TacoOrder;
import com.example.springinaction.tacoapp.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KitchenService {

    private final OrderService orderService;

    public List<TacoOrder> getOrdersInQueue() {
        return orderService.getOrdersWithStatus(OrderStatus.CREATED);
    }

    public List<TacoOrder> getActiveOrders() {
        return orderService.getOrdersWithStatus(OrderStatus.COOKING);
    }

    public void startCooking(Long orderId) {
        orderService.changeTacoOrderStatus(orderId, OrderStatus.COOKING);
    }

    public void completeCooking(Long orderId) {
        orderService.changeTacoOrderStatus(orderId, OrderStatus.READY);
    }
}
