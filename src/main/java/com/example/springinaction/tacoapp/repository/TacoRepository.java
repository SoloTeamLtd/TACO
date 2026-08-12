package com.example.springinaction.tacoapp.repository;

import com.example.springinaction.tacoapp.entity.Taco;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TacoRepository extends CrudRepository<Taco, Long> {
}
