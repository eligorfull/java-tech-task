package com.rezdy.lunch;

import com.rezdy.lunch.repository.RecipeRepository;
import com.rezdy.lunch.service.Recipe;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
@DataJpaTest
public class RecipeRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RecipeRepository recipeRepository;

    @Test
    public void testFindRecipeByTitle() {
        Recipe recipe = new Recipe();
        recipe.setTitle("Noodles");
        entityManager.persist(recipe);
        entityManager.flush();

        Recipe recipeFound = recipeRepository.findByTitle(recipe.getTitle());
        assertTrue("The found recipe should have the same title", recipeFound.getTitle().equals(recipe.getTitle()));
    }

    @Test
    public void testNotFoundRecipeByTitle() {
        Recipe recipe = new Recipe();
        recipe.setTitle("Noodles");
        Recipe recipeFound = recipeRepository.findByTitle(recipe.getTitle());

        assertNull("This recipe should not exist", recipeFound);
    }

}
