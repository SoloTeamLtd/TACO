package com.example.springinaction.tacoapp.controller;

import com.example.springinaction.tacoapp.order.controller.TacoOrderController;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(TacoOrderController.class)
public class TacoOrderControllerTest {
/*
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TacoOrderRepository tacoOrderRepository;

    @MockitoBean
    private IngredientRepository ingredientRepository;

    @MockitoBean
    private TacoRepository tacoRepository;

    @Test
    void testOrderForm() throws Exception {
        mockMvc.perform(get("/orders/current"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderForm"))
                .andExpect(model().attributeExists("tacoOrder"));
    }

    @Test
    void testProcessOrderWithValidData() throws Exception {
        // Настраиваем мок репозитория на возврат любого сохраненного объекта
        Mockito.when(tacoOrderRepository.save(any(TacoOrder.class))).thenReturn(new TacoOrder());

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        // Передаем валидные параметры формы (согласно аннотациям в TacoOrder)
                        .param("deliveryName", "Craig Walls")
                        .param("deliveryStreet", "123 Tomato Rd")
                        .param("deliveryCity", "Fayetteville")
                        .param("deliveryState", "AR")
                        .param("deliveryZip", "72701")
                        // Пример валидной тестовой карты (проходит алгоритм Луна для @CreditCardNumber)
                        .param("ccNumber", "4111111111111111")
                        .param("ccExpiration", "12/30")
                        .param("ccCvv", "123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        // Проверяем, что метод save действительно вызвался 1 раз
        Mockito.verify(tacoOrderRepository, Mockito.times(1)).save(any(TacoOrder.class));
    }

    @Test
    void testProcessOrderWithInvalidData() throws Exception {
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        // Отправляем пустые поля для проверки валидации @NotBlank / @NotNull
                        .param("deliveryName", "")
                        .param("deliveryStreet", "")
                        .param("ccNumber", "invalid-card"))
                .andExpect(status().isOk()) // Должна вернуться та же страница
                .andExpect(view().name("orderForm"))
                .andExpect(model().hasErrors()); // Проверяем, что зафиксированы ошибки валидации

        // Репозиторий НЕ должен вызываться, если данные невалидны
        Mockito.verify(tacoOrderRepository, Mockito.never()).save(any(TacoOrder.class));
    }*/
}
