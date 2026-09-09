package com.example.springinaction.tacoapp.controller;

import com.example.springinaction.tacoapp.entity.Ingredient;
import com.example.springinaction.tacoapp.entity.IngredientType;
import com.example.springinaction.tacoapp.entity.Taco;
import com.example.springinaction.tacoapp.entity.TacoOrder;
import com.example.springinaction.tacoapp.repository.IngredientRepository;
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
    public String processTaco(@Valid Taco taco, Errors errors,
                              @ModelAttribute TacoOrder tacoOrder) {
        if (errors.hasErrors()) {
            return "design";
        }

        tacoOrder.getTacos().add(taco);
        return "redirect:/orders/current";
    }
}
