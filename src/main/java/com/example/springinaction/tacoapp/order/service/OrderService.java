package com.example.springinaction.tacoapp.order.service;

import com.example.springinaction.tacoapp.core.domain.entity.OrderStatus;
import com.example.springinaction.tacoapp.core.domain.entity.OrderStatusHistory;
import com.example.springinaction.tacoapp.core.domain.entity.TacoOrder;
import com.example.springinaction.tacoapp.order.repository.OrderStatusHistoryRepository;
import com.example.springinaction.tacoapp.order.repository.TacoOrderRepository;
import com.example.springinaction.tacoapp.security.SecurityContextHelper;
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
    private final OrderStatusHistoryRepository statusHistoryRepository;
    private final OrderStateMachine orderStateMachine;
    private final SecurityContextHelper securityContextHelper;

    public void changeTacoOrderStatus(Long orderId, OrderStatus targetStatus) {
        TacoOrder tacoOrder = tacoOrderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Заказ с ID " + orderId + " не найден"));

        String currentUsername = securityContextHelper.getCurrentUsername();
        OrderStatusHistory historyEntry = orderStateMachine.transition(tacoOrder, targetStatus, currentUsername);
        tacoOrderRepository.save(tacoOrder);
        statusHistoryRepository.save(historyEntry);
        log.info("Пользователь [{}] успешно перевел заказ id={} в статус {}", currentUsername, orderId, targetStatus);
    }

    public TacoOrder createOrder(TacoOrder tacoOrder) {
        tacoOrder.setStatus(OrderStatus.CREATED);
        TacoOrder savedOrder = tacoOrderRepository.save(tacoOrder);
        log.info("Новый заказ успешно создан и сохранен в БД. ID: {}", savedOrder.getId());
        return savedOrder;
    }

    @Transactional(readOnly = true)
    public List<TacoOrder> getOrdersWithStatus(OrderStatus status) {
        return tacoOrderRepository.findByStatus(status);
    }

    public TacoOrder getTacoOrder(Long orderId) {
        TacoOrder tacoOrder = tacoOrderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Заказ " + orderId + " не найден"));
        return tacoOrder;
    }
}
