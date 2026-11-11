package com.example.springinaction.tacoapp.controller;

import com.example.springinaction.tacoapp.order.controller.DesignTacoController;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(DesignTacoController.class)
class DesignTacoControllerTest {
/*

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
                new Ingredient("CHED", "Cheddar", IngredientType.CHEESE),
                new Ingredient("SLSA", "Salsa", IngredientType.SAUCE)
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
                .andExpect(model().attributeExists("cheese"))
                .andExpect(model().attributeExists("sauce"))
                .andExpect(model().attributeExists("taco"));
    }

    //TODO include processTaco
*/

}
