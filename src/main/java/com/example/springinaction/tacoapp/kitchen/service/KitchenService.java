package com.example.springinaction.tacoapp.kitchen.service;

import com.example.springinaction.tacoapp.core.domain.entity.OrderStatus;
import com.example.springinaction.tacoapp.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KitchenService {

    private final OrderService orderService;

    public void startCooking(Long orderId) {
        orderService.changeTacoOrderStatus(orderId, OrderStatus.COOKING);
    }

    public void completeCooking(Long orderId) {
        orderService.changeTacoOrderStatus(orderId, OrderStatus.READY);
    }
}
