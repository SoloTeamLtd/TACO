package com.example.springinaction.tacoapp.order.controller;

import com.example.springinaction.tacoapp.core.domain.entity.taco.Ingredient;
import com.example.springinaction.tacoapp.core.domain.entity.taco.IngredientType;
import com.example.springinaction.tacoapp.core.domain.entity.taco.Taco;
import com.example.springinaction.tacoapp.core.domain.entity.taco.TacoOrder;
import com.example.springinaction.tacoapp.core.domain.repository.taco.IngredientRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("tacoOrder")
@AllArgsConstructor
public class DesignTacoController {

    private final IngredientRepository ingredientRepository;

    @ModelAttribute(name = "tacoOrder")
    public TacoOrder order() {
        return new TacoOrder();
    }

    @ModelAttribute
    public void addIngredientsToModel(Model model) {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredientRepository.findAll().forEach(ingredients::add);
        IngredientType[] types = IngredientType.values();
        for (IngredientType type: types) {
            model.addAttribute(type.name().toLowerCase(),
                    filterByType(ingredients, type));
        }
    }

    @GetMapping
    public String showDesignForm(Model model) {
        model.addAttribute("taco", new Taco());
        return "design";
    }

    private List<Ingredient> filterByType(List<Ingredient> ingredients, IngredientType type) {
            return ingredients.stream()
                    .filter(x -> x.getType().equals(type))
                    .collect(Collectors.toList());
    }

    @PostMapping
    public String processTaco(@Valid @ModelAttribute("taco") Taco taco, Errors errors,
                              @ModelAttribute TacoOrder tacoOrder) {
        if (errors.hasErrors()) {
            return "design";
        }

        tacoOrder.getTacos().add(taco);
        log.info("Processing taco: {} ", taco);

        return "redirect:/orders/current";
    }
}
