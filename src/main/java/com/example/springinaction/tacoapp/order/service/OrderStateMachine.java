package com.example.springinaction.tacoapp.order.service;

import com.example.springinaction.tacoapp.order.domain.OrderStatus;
import com.example.springinaction.tacoapp.order.domain.OrderStatusHistory;
import com.example.springinaction.tacoapp.order.domain.TacoOrder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Component
public class OrderStateMachine {

    private static final Map<OrderStatus, List<OrderStatus>> VALID_TRANSITIONS = Map.of(
            OrderStatus.CREATED, List.of(OrderStatus.COOKING, OrderStatus.CANCELLED),
            OrderStatus.COOKING, List.of(OrderStatus.READY, OrderStatus.CANCELLED),
            OrderStatus.READY, List.of(OrderStatus.DELIVERING, OrderStatus.CANCELLED),
            OrderStatus.DELIVERING, List.of(OrderStatus.DELIVERED, OrderStatus.CANCELLED),
            OrderStatus.DELIVERED, List.of(),
            OrderStatus.CANCELLED, List.of()
    );

    public OrderStatusHistory transition(TacoOrder order, OrderStatus targetStatus, String userInitiator) {
        OrderStatus currentStatus = order.getStatus();

        // Валидация перехода по графу
        List<OrderStatus> allowedStatuses = VALID_TRANSITIONS.get(currentStatus);
        if (allowedStatuses == null || !allowedStatuses.contains(targetStatus)) {
            throw new IllegalStateException(
                    String.format("Запрещенный переход по бизнес-процессу: из %s в %s", currentStatus, targetStatus)
            );
        }

        checkSecurityPermissions(targetStatus);
        order.setStatus(targetStatus);
        return new OrderStatusHistory(order, currentStatus, targetStatus, userInitiator);
    }

    private void checkSecurityPermissions(OrderStatus targetStatus) {
        var authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        if (authorities.contains("ROLE_ADMIN")) {
            return;
        }

        switch (targetStatus) {
            case COOKING, READY -> {
                if (!authorities.contains("ROLE_KITCHEN")) {
                    throw new org.springframework.security.access
                            .AccessDeniedException("Только сотрудники кухни могут управлять приготовлением!");
                }
            }
            case DELIVERING, DELIVERED -> {
                if (!authorities.contains("ROLE_DELIVERY")) {
                    throw new org.springframework.security.access
                            .AccessDeniedException("Только служба доставки может управлять этапом транспортировки!");
                }
            }
            case CANCELLED -> {
                if (!authorities.contains("ROLE_USER") && !authorities.contains("ROLE_KITCHEN")
                        && !authorities.contains("ROLE_ADMIN")) {
                    throw new org.springframework.security.access
                            .AccessDeniedException("Отмена невозможна!");
                }
            }
        }
    }
}
