package com.rezdy.lunch;

import com.rezdy.lunch.repository.RecipeRepository;
import com.rezdy.lunch.service.Ingredient;
import com.rezdy.lunch.service.LunchService;
import com.rezdy.lunch.service.Recipe;
import com.rezdy.lunch.service.impl.LunchServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LunchServiceImplIntegrationTest {

    @TestConfiguration
    static class LunchServiceImplTestContextConfiguration {

        @Bean
        public LunchService lunchService() {
            return new LunchServiceImpl();
        }
    }
    @Autowired
    private LunchService lunchService;

    @MockBean
    private RecipeRepository recipeRepository;

    @Before
    public void setUp() {
        Recipe recipe = new Recipe();
        recipe.setTitle("Salad");
        Mockito.when(recipeRepository.findByTitle(recipe.getTitle())).thenReturn(recipe);
    }

    @Before
    public void setUpRecipes() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String oldDateStr = "2019-01-06";
        LocalDate oldDate = LocalDate.parse(oldDateStr, formatter);

        String goodDateStr = "2021-01-20";
        LocalDate goodDate = LocalDate.parse(goodDateStr, formatter);

        String bestBeforeDateStr = "2021-01-01";
        LocalDate bestBeforeDate = LocalDate.parse(bestBeforeDateStr, formatter);

        Ingredient tomatoIngredient = new Ingredient();
        tomatoIngredient.setTitle("Tomato");
        tomatoIngredient.setUseBy(goodDate);
        tomatoIngredient.setBestBefore(goodDate);

        Ingredient lettuceIngredient = new Ingredient();
        lettuceIngredient.setTitle("Lettuce");
        lettuceIngredient.setUseBy(goodDate);
        lettuceIngredient.setBestBefore(bestBeforeDate); // Best before should be sort it at the bottom

        Ingredient avocadoIngredient = new Ingredient();
        avocadoIngredient.setTitle("Avocado");
        avocadoIngredient.setUseBy(goodDate);
        avocadoIngredient.setBestBefore(goodDate);

        Ingredient cheeseIngredient = new Ingredient();
        cheeseIngredient.setTitle("Cheese");
        cheeseIngredient.setUseBy(goodDate);
        cheeseIngredient.setBestBefore(goodDate);

        Ingredient meatIngredient = new Ingredient();
        meatIngredient.setTitle("Meat");
        meatIngredient.setUseBy(goodDate);
        meatIngredient.setBestBefore(goodDate);

        Ingredient bunIngredient = new Ingredient();
        bunIngredient.setTitle("Bun");
        bunIngredient.setUseBy(goodDate);
        bunIngredient.setBestBefore(goodDate);

        Ingredient tomatoSauceIngredient = new Ingredient();
        tomatoSauceIngredient.setTitle("Tomato Sauce");
        tomatoSauceIngredient.setUseBy(oldDate); // Out of date
        tomatoSauceIngredient.setBestBefore(goodDate);

        Ingredient sausageIngredient = new Ingredient();
        sausageIngredient.setTitle("Sausage");
        sausageIngredient.setUseBy(goodDate);
        sausageIngredient.setBestBefore(goodDate);

        Recipe saladRecipe = new Recipe();
        saladRecipe.setTitle("Salad");
        List saladIngredients = new ArrayList();
        saladIngredients.add(tomatoIngredient);
        saladIngredients.add(lettuceIngredient);
        saladIngredients.add(avocadoIngredient);
        saladRecipe.setIngredients(new HashSet<Ingredient>(saladIngredients));

        Recipe hamburgerRecipe = new Recipe();
        hamburgerRecipe.setTitle("Hamburger");
        List hamburgerIngredients = new ArrayList();
        hamburgerIngredients.add(meatIngredient);
        hamburgerIngredients.add(bunIngredient);
        hamburgerIngredients.add(cheeseIngredient);
        hamburgerIngredients.add(tomatoSauceIngredient);
        hamburgerRecipe.setIngredients(new HashSet<Ingredient>(hamburgerIngredients));

        Recipe hotDogRecipe = new Recipe();
        hotDogRecipe.setTitle("HotDog");
        List hotDogIngredients = new ArrayList();
        hotDogIngredients.add(bunIngredient);
        hotDogIngredients.add(cheeseIngredient);
        hotDogIngredients.add(sausageIngredient);
        hotDogIngredients.add(avocadoIngredient);
        hotDogRecipe.setIngredients(new HashSet<Ingredient>(hotDogIngredients));

        List<Recipe> recipes = new ArrayList<>();
        recipes.add(saladRecipe);
        recipes.add(hamburgerRecipe);
        recipes.add(hotDogRecipe);

        Mockito.when(recipeRepository.findAll()).thenReturn(recipes);
    }

    @Test
    public void testGetRecipesWithNonExpiredIngredientsShouldBeFound() {
        String name = "Salad";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = "2020-06-06";

        //convert String to LocalDate
        LocalDate localDate = LocalDate.parse(date, formatter);
        List<Recipe> foundRecipes = lunchService.getNonExpiredRecipesOnDate(localDate, null);

        assertNotNull("It should find some recipes", foundRecipes);
        assertEquals("It should have two valid recipes", 2, foundRecipes.size());

        assertEquals("Hot dog should be at the beginning of the list cuz all ingredients are fresh", "HotDog", foundRecipes.get(0).getTitle());
        assertEquals("Salad should be at the end of the list cuz lettuce is expiring soon", "Salad", foundRecipes.get(1).getTitle());
    }

    @Test
    public void testExcludeRecipesFromTheList() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = "2020-06-06";
        String excludeStr = "Tomato"; // it should exclude the Salad recipe cuz it has tomato in it

        //convert String to LocalDate
        LocalDate localDate = LocalDate.parse(date, formatter);
        List<Recipe> foundRecipes = lunchService.getNonExpiredRecipesOnDate(localDate, excludeStr);

        assertNotNull("It should find some recipes", foundRecipes);
        assertEquals("It should have only one valid recipe", 1, foundRecipes.size());

        assertEquals("Hot dog should be at the beginning of the list cuz all ingredients are fresh", "HotDog", foundRecipes.get(0).getTitle());
    }

    @Test
    public void testValidTitleThenRecipeShouldBeFound() {
        String name = "Salad";
        Recipe foundRecipe = lunchService.getRecipeByTitle(name);

        assertNotNull("It should find a salad recipe", foundRecipe);
        assertTrue("It should find the Salad recipe", foundRecipe.getTitle().equals(name));
    }

    @Test
    public void testInvalidTitleThenRecipeShouldNotBeFound() {
        String name = "Ensalada";
        Recipe foundRecipe = lunchService.getRecipeByTitle(name);

        assertNull("It should not find a salad recipe", foundRecipe);
    }

}
