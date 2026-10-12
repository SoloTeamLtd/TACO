package com.example.springinaction.tacoapp.domain.customer.repository;

import com.example.springinaction.tacoapp.domain.customer.entity.Taco;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TacoRepository extends CrudRepository<Taco, Long> {
}
