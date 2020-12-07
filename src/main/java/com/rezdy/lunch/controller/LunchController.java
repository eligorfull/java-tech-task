package com.rezdy.lunch.controller;

import com.rezdy.lunch.service.LunchService;
import com.rezdy.lunch.service.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
public class LunchController {

    private LunchService lunchService;

    @Autowired
    public LunchController(LunchService lunchService) {
        this.lunchService = lunchService;
    }

    @GetMapping("/lunch")
    public List<Recipe> getRecipes(@RequestParam(value = "date") String date) {
        return lunchService.getNonExpiredRecipesOnDate(LocalDate.parse(date), null);
    }

    @GetMapping("/recipe")
    public Recipe getRecipeByTitle(@RequestParam(value = "title") String title) {
        Recipe recipe = lunchService.getRecipeByTitle(title);
        if (recipe == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Recipe not found"
            );
        }
        return recipe;
    }

    @GetMapping("/exclude")
    public List<Recipe> excludeRecipes(@RequestParam(value = "date") String date,
                                 @RequestParam(value = "exclude") String exclude) {
        return lunchService.getNonExpiredRecipesOnDate(LocalDate.parse(date), exclude);
    }
}
