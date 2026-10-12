package com.example.springinaction.tacoapp.domain.customer;

import com.example.springinaction.tacoapp.domain.customer.entity.OrderStatus;
import com.example.springinaction.tacoapp.domain.customer.entity.TacoOrder;
import com.example.springinaction.tacoapp.domain.customer.repository.TacoOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final TacoOrderRepository tacoOrderRepository;

    @Transactional(readOnly = true)
    public List<TacoOrder> getOrdersWithStatus(OrderStatus status) {
        return tacoOrderRepository.findByStatus(status);
    }

    public void changeTacoOrderStatus(TacoOrder tacoOrder, OrderStatus requiredStatus,
                                       OrderStatus finalStatus) {
        if (tacoOrder.getStatus() != requiredStatus) {
            throw new IllegalArgumentException("Статус заказа не соответствует " + requiredStatus);
        }
        tacoOrder.setStatus(finalStatus);
        tacoOrderRepository.save(tacoOrder);
    }

    public TacoOrder getTacoOrder(Long orderId) {
        TacoOrder tacoOrder = tacoOrderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Заказ " + orderId + " не найден"));
        return tacoOrder;
    }
}
