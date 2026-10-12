package com.example.springinaction.tacoapp.domain.kitchen;

import com.example.springinaction.tacoapp.domain.customer.OrderService;
import com.example.springinaction.tacoapp.domain.customer.entity.TacoOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.springinaction.tacoapp.domain.customer.entity.OrderStatus;

@Service
@RequiredArgsConstructor
@Transactional
public class KitchenOrderService {

    private final OrderService orderService;

    public void startCooking(Long orderId) {
        TacoOrder tacoOrder = orderService.getTacoOrder(orderId);
        orderService.changeTacoOrderStatus(tacoOrder, OrderStatus.CREATED, OrderStatus.COOKING);
    }

    public void completeCooking(Long orderId) {
        TacoOrder tacoOrder = orderService.getTacoOrder(orderId);
        orderService.changeTacoOrderStatus(tacoOrder, OrderStatus.COOKING, OrderStatus.READY);
    }
}
