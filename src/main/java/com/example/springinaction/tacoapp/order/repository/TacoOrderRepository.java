package com.example.springinaction.tacoapp.order.repository;

import com.example.springinaction.tacoapp.order.domain.OrderStatus;
import com.example.springinaction.tacoapp.order.domain.TacoOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TacoOrderRepository extends CrudRepository<TacoOrder, Long> {

    List<TacoOrder> findByStatus(OrderStatus status);
    Page<TacoOrder> findByUserId(Long currentUserId, Pageable pageable);
}
