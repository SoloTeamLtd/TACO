package com.example.springinaction.tacoapp.order.repository;

import com.example.springinaction.tacoapp.core.domain.entity.OrderStatus;
import com.example.springinaction.tacoapp.core.domain.entity.TacoOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TacoOrderRepository extends CrudRepository<TacoOrder, Long> {

    List<TacoOrder> findByStatus(OrderStatus status);
}
