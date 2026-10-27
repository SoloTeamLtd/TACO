package com.example.springinaction.tacoapp.ingredient.repository;

import com.example.springinaction.tacoapp.ingredient.domain.Ingredient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends CrudRepository<Ingredient, String> {
}
