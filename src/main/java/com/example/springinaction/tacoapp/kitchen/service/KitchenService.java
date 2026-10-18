package com.example.springinaction.tacoapp.kitchen.service;

import com.example.springinaction.tacoapp.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.example.springinaction.tacoapp.core.domain.entity.OrderStatus;

@Service
@RequiredArgsConstructor
@Slf4j
public class KitchenService {

    private final OrderService orderService;

    public void startCooking(Long orderId) {
        orderService.changeTacoOrderStatus(orderId, OrderStatus.CREATED, OrderStatus.COOKING);
    }

    public void completeCooking(Long orderId) {
        orderService.changeTacoOrderStatus(orderId, OrderStatus.COOKING, OrderStatus.READY);
    }
}
