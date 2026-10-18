package com.example.springinaction.tacoapp.core.domain.repository;

import com.example.springinaction.tacoapp.core.domain.entity.Ingredient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends CrudRepository<Ingredient, String> {
}
