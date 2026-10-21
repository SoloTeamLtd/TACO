package com.example.springinaction.tacoapp.order.repository;

import com.example.springinaction.tacoapp.core.domain.entity.OrderStatusHistory;
import com.example.springinaction.tacoapp.core.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory, Long> {

    Optional<OrderStatusHistory> findByOrderId(Long orderId);
}
