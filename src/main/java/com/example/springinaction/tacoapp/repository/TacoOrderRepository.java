package com.example.springinaction.tacoapp.repository;

import com.example.springinaction.tacoapp.entity.TacoOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TacoOrderRepository extends CrudRepository<TacoOrder, Long> {
}
