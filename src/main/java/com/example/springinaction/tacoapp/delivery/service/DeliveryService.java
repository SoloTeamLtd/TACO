package com.example.springinaction.tacoapp.delivery.service;

import com.example.springinaction.tacoapp.auth.service.SecurityContextHelper;
import com.example.springinaction.tacoapp.order.service.OrderService;
import com.example.springinaction.tacoapp.order.domain.OrderStatus;
import com.example.springinaction.tacoapp.order.domain.TacoOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryService {

    private final OrderService orderService;
    private final SecurityContextHelper securityContextHelper;

    public List<TacoOrder> getOrdersReadyForDelivery() {
        return orderService.getOrdersWithStatus(OrderStatus.READY);
    }

    public List<TacoOrder> getOrdersInDelivery() {
        return orderService.getOrdersWithStatus(OrderStatus.DELIVERING);
    }

    /**
     * Заказы, назначенные текущему курьеру (личная страница).
     */
    public List<TacoOrder> getMyActiveDeliveries() {
        Long courierId = securityContextHelper.getCurrentUserId();
        log.info("Запрос активных доставок для курьера userId={}", courierId);
        return orderService.getDeliveriesForCourier(courierId, OrderStatus.DELIVERING);
    }

    @Transactional
    public void takeOrder(Long orderId) {
        Long courierId = securityContextHelper.getCurrentUserId();
        log.info("Курьер userId={} берёт заказ ID={}", courierId, orderId);
        orderService.assignOrderToCourierAndChangeStatus(orderId, courierId, OrderStatus.DELIVERING);
    }

    public void completeDelivery(Long orderId) {
        orderService.changeTacoOrderStatus(orderId, OrderStatus.DELIVERED);
    }
}
