package com.rezdy.lunch.service;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
public class Recipe implements Comparable<Recipe> {

    @Id
    private String title;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "recipe_ingredient",
            joinColumns = @JoinColumn(name = "recipe"),
            inverseJoinColumns = @JoinColumn(name = "ingredient"))
    private Set<Ingredient> ingredients;

    public String getTitle() {
        return title;
    }

    public Recipe setTitle(String title) {
        this.title = title;
        return this;
    }

    public Set<Ingredient> getIngredients() {
        return ingredients;
    }

    public Recipe setIngredients(Set<Ingredient> ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    @Override
    public int compareTo(Recipe o) {
        int comp = o.lowestBestBefore().compareTo(this.lowestBestBefore());
        return comp;
    }

    private LocalDate lowestBestBefore() {
        List<LocalDate> dates = new ArrayList<>();
        for(Ingredient ingredient : ingredients){
            dates.add(ingredient.getBestBefore());
        }
        final LocalDate minDate = dates.stream()
                .min(LocalDate::compareTo)
                .get();
        return minDate;
    }
}
