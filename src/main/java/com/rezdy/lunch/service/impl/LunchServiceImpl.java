package com.rezdy.lunch.service.impl;

import com.rezdy.lunch.repository.RecipeRepository;
import com.rezdy.lunch.service.Ingredient;
import com.rezdy.lunch.service.LunchService;
import com.rezdy.lunch.service.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.*;

@Service
public class LunchServiceImpl implements LunchService {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private RecipeRepository recipeRepository;

    public List<Recipe> getNonExpiredRecipesOnDate(LocalDate date, String excludeStr) {
        List<String> exclude = null;
        if (excludeStr != null) {
            exclude = Arrays.asList(excludeStr.split(","));
        }
        List<Recipe> recipes = loadRecipes(date, exclude);
        return recipes;
    }

    public Recipe getRecipeByTitle(String title) {
        return recipeRepository.findByTitle(title);
    }

    private List<Recipe> loadRecipes(LocalDate date, List<String> exclude) {
        List<Recipe> possibleReceipts = new ArrayList<>();
        List<Recipe> recipes = recipeRepository.findAll();
        if (recipes != null && recipes.size() > 0) {
            for (Recipe recipe : recipes) {
                boolean isExpired = this.containsExpiredIngredient(recipe, date, exclude);
                if (!isExpired) {
                    possibleReceipts.add(recipe);
                }
            }
        }

        Collections.sort(possibleReceipts);
        return possibleReceipts;
    }

    private boolean containsExpiredIngredient(Recipe recipe, LocalDate date, List<String> exclude) {
        if (recipe.getIngredients() != null && recipe.getIngredients().size() > 0) {
            for (Ingredient ingredient : recipe.getIngredients()) {
                if (ingredient.getUseBy().isBefore(date)) {
                    return true;
                }
                if (exclude != null && exclude.contains(ingredient.getTitle())) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

}
