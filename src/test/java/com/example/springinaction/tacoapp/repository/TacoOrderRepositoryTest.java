package com.example.springinaction.tacoapp.repository;

import com.example.springinaction.tacoapp.entity.Ingredient;
import com.example.springinaction.tacoapp.entity.Taco;
import com.example.springinaction.tacoapp.entity.TacoOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TacoOrderRepositoryTest {

    @Autowired
    private TacoOrderRepository tacoOrderRepository;
    @Autowired
    private IngredientRepository ingredientRepository;

    @Test
    void shouldSaveOrderWithTacosAndIngredients() {
        // 1. Test that Liquibase set ingredients
        Optional<Ingredient> flourTortilla = ingredientRepository.findById("FLTO");
        Optional<Ingredient> groundBeef = ingredientRepository.findById("GRBF");

        assertTrue(flourTortilla.isPresent(), "Ingredient FLTO should be ib BD");
        assertTrue(groundBeef.isPresent(), "Ingredient GRBF should be ib BD");

        // 2. Create Taco
        Taco taco = new Taco();
        taco.setName("The Monster Taco");
        taco.getIngredients().add(flourTortilla.get());
        taco.getIngredients().add(groundBeef.get());

        // 3. Create order and include Taco
        TacoOrder order = new TacoOrder();
        order.setDeliveryName("Ivan Dev");
        order.setDeliveryStreet("Java Avenue");
        order.setDeliveryCity("Saint-Petersburg");
        order.setDeliveryState("SPb");
        order.setDeliveryZip("190000");
        order.setCcNumber("4111111111111111");
        order.setCcExpiration("12/30");
        order.setCcCvv("123");

        order.getTacos().add(taco);

        // 4. Create order on DB and check saved orders
        TacoOrder savedOrder = tacoOrderRepository.save(order);
        assertNotNull(savedOrder.getId(), "Should be Id");
        assertEquals(1, savedOrder.getTacos().size());

        // 5. Get saved taco and check saved tacos
        Taco savedTaco = savedOrder.getTacos().get(0);
        assertNotNull(savedTaco.getId(), "Should be Id");
        assertEquals("The Monster Taco", savedTaco.getName());
        assertEquals(2, savedTaco.getIngredients().size());
    }
}
