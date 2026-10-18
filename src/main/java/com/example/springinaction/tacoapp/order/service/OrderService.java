package com.example.springinaction.tacoapp.order.service;

import com.example.springinaction.tacoapp.core.domain.entity.OrderStatus;
import com.example.springinaction.tacoapp.core.domain.entity.TacoOrder;
import com.example.springinaction.tacoapp.order.repository.TacoOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final TacoOrderRepository tacoOrderRepository;

    @Transactional(readOnly = true)
    public List<TacoOrder> getOrdersWithStatus(OrderStatus status) {
        return tacoOrderRepository.findByStatus(status);
    }

    public void changeTacoOrderStatus(Long orderId, OrderStatus requiredStatus,
                                       OrderStatus finalStatus) {
        TacoOrder tacoOrder = tacoOrderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Заказ с ID " + orderId + " не найден"));
        if (tacoOrder.getStatus() != requiredStatus) {
            throw new IllegalArgumentException("Статус заказа не соответствует " + requiredStatus);
        }
        tacoOrder.setStatus(finalStatus);
        tacoOrderRepository.save(tacoOrder);
        log.info("Заказ id={} переведен в статус {}", orderId, finalStatus);
    }

    public TacoOrder getTacoOrder(Long orderId) {
        TacoOrder tacoOrder = tacoOrderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Заказ " + orderId + " не найден"));
        return tacoOrder;
    }
}
