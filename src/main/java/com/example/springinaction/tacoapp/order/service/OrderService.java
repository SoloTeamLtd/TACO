package com.example.springinaction.tacoapp.order.service;

import com.example.springinaction.tacoapp.order.domain.OrderStatus;
import com.example.springinaction.tacoapp.order.domain.OrderStatusHistory;
import com.example.springinaction.tacoapp.order.domain.TacoOrder;
import com.example.springinaction.tacoapp.order.repository.OrderStatusHistoryRepository;
import com.example.springinaction.tacoapp.order.repository.TacoOrderRepository;
import com.example.springinaction.tacoapp.auth.service.SecurityContextHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        Long currentUserId = securityContextHelper.getCurrentUserId();
        tacoOrder.setUserId(currentUserId);
        tacoOrder.setStatus(OrderStatus.CREATED);
        TacoOrder savedOrder = tacoOrderRepository.save(tacoOrder);
        log.info("Новый заказ успешно создан и сохранен в БД. ID: {}, для userId: {}",
                savedOrder.getId(), currentUserId);
        return savedOrder;
    }

    @Transactional(readOnly = true)
    public Page<TacoOrder> getOrdersForCurrentUser(int page, int size) {
        Long currentUserId = securityContextHelper.getCurrentUserId();
        log.info("Запрос списка заказов из taco_db для userId [{}], страница: {}, размер: {}", currentUserId, page, size);

        // Сортировка по id в обратном порядке (сначала новые заказы)
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return tacoOrderRepository.findByUserId(currentUserId, pageable);
    }

    @Transactional(readOnly = true)
    public List<TacoOrder> getOrdersWithStatus(OrderStatus status) {
        return tacoOrderRepository.findByStatus(status);
    }

    /**
     * Заказы, назначенные конкретному курьеру с указанным статусом.
     */
    @Transactional(readOnly = true)
    public List<TacoOrder> getDeliveriesForCourier(Long courierId, OrderStatus status) {
        return tacoOrderRepository.findByDeliveryPersonIdAndStatus(courierId, status);
    }

    @Transactional
    public void assignOrderToCourierAndChangeStatus(Long orderId, Long courierId, OrderStatus targetStatus) {
        TacoOrder tacoOrder = tacoOrderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Заказ с ID " + orderId + " не найден"));

        tacoOrder.setDeliveryPersonId(courierId);

        String currentUsername = securityContextHelper.getCurrentUsername();
        OrderStatusHistory historyEntry = orderStateMachine.transition(tacoOrder, targetStatus, currentUsername);
        tacoOrderRepository.save(tacoOrder);
        statusHistoryRepository.save(historyEntry);
        log.info("Курьер userId={} назначен на заказ id={}, статус изменён на {}", courierId, orderId, targetStatus);
    }

    public TacoOrder getTacoOrder(Long orderId) {
        TacoOrder tacoOrder = tacoOrderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Заказ " + orderId + " не найден"));
        return tacoOrder;
    }
}
