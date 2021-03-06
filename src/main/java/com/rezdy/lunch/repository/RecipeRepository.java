package com.rezdy.lunch.repository;

import com.rezdy.lunch.service.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, String> {

    Recipe findByTitle(String title);
}
