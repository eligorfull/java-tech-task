package com.rezdy.lunch.repository;

import com.rezdy.lunch.service.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, String> {
}
