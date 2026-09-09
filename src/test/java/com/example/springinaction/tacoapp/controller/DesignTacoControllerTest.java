package com.example.springinaction.tacoapp.controller;

import com.example.springinaction.tacoapp.entity.Ingredient;
import com.example.springinaction.tacoapp.entity.IngredientType;
import com.example.springinaction.tacoapp.repository.IngredientRepository;
import com.example.springinaction.tacoapp.repository.TacoOrderRepository;
import com.example.springinaction.tacoapp.repository.TacoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DesignTacoController.class)
class DesignTacoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IngredientRepository ingredientRepository;

    @MockitoBean
    private TacoRepository tacoRepository;

    @MockitoBean
    private TacoOrderRepository tacoOrderRepository;

    private List<Ingredient> ingredients;

    @BeforeEach
    void setup() {
        ingredients = Arrays.asList(
                new Ingredient("FLTO", "Flour Tortilla", IngredientType.WRAP),
                new Ingredient("GRBF", "Ground Beef", IngredientType.PROTEIN),
                new Ingredient("CHED", "Cheddar", IngredientType.CHEESE)
        );
    }

    @Test
    void testShowDesignForm() throws Exception {
        Mockito.when(ingredientRepository.findAll()).thenReturn(ingredients);

        mockMvc.perform(get("/design"))
                .andExpect(status().isOk())
                .andExpect(view().name("design"))
                .andExpect(model().attributeExists("wrap"))
                .andExpect(model().attributeExists("protein"))
                .andExpect(model().attributeExists("chess"))
                .andExpect(model().attributeExists("taco"));
    }

    //TODO include processTaco
}
