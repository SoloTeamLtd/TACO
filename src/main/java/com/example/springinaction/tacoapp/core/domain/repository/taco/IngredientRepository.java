package com.example.springinaction.tacoapp.core.domain.repository.taco;

import com.example.springinaction.tacoapp.core.domain.entity.taco.Ingredient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends CrudRepository<Ingredient, String> {
}
