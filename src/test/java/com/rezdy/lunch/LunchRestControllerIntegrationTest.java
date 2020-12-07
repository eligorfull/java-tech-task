package com.rezdy.lunch;

import com.rezdy.lunch.controller.LunchController;
import com.rezdy.lunch.service.LunchService;
import com.rezdy.lunch.service.Recipe;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration()
@WebMvcTest(LunchController.class)
public class LunchRestControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private LunchService lunchService;

    @Test
    public void givenRecipes_whenGetRecipes_thenReturnJsonArray() throws Exception {
        Recipe saladRecipe = new Recipe();
        saladRecipe.setTitle("Salad");
        Recipe fryUpRecipe = new Recipe();
        fryUpRecipe.setTitle("Fry-up");
        Recipe hotDogRecipe = new Recipe();
        hotDogRecipe.setTitle("Hotdog");
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(saladRecipe);
        recipes.add(fryUpRecipe);
        recipes.add(hotDogRecipe);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = "2020-06-06";

        //convert String to LocalDate
        LocalDate localDate = LocalDate.parse(date, formatter);

        given(lunchService.getNonExpiredRecipesOnDate(localDate, null)).willReturn(recipes);

        mvc.perform(get("/lunch?date=" + date)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].title", is(saladRecipe.getTitle())));
    }

}
