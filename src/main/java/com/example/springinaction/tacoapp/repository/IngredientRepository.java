package com.example.springinaction.tacoapp.repository;

import com.example.springinaction.tacoapp.entity.Ingredient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends CrudRepository<Ingredient, String> {
}
