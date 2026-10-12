package com.example.springinaction.tacoapp.domain.delivery;

import com.example.springinaction.tacoapp.domain.customer.OrderService;
import com.example.springinaction.tacoapp.domain.customer.entity.OrderStatus;
import com.example.springinaction.tacoapp.domain.customer.entity.TacoOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryService {

    private final OrderService orderService;

    public void takeOrder(Long orderId) {
        TacoOrder tacoOrder = orderService.getTacoOrder(orderId);
        orderService.changeTacoOrderStatus(tacoOrder, OrderStatus.READY, OrderStatus.DELIVERING);
    }

    public void completeDelivery(Long orderId) {
        TacoOrder tacoOrder = orderService.getTacoOrder(orderId);
        orderService.changeTacoOrderStatus(tacoOrder, OrderStatus.DELIVERING, OrderStatus.DELIVERED);
    }
}
