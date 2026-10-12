package com.example.springinaction.tacoapp.domain.shared.repository;

import com.example.springinaction.tacoapp.domain.shared.entity.Ingredient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends CrudRepository<Ingredient, String> {
}
