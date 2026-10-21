package com.example.springinaction.tacoapp.kitchen.controller;

import com.example.springinaction.tacoapp.kitchen.service.KitchenService;
import com.example.springinaction.tacoapp.order.service.OrderService;
import com.example.springinaction.tacoapp.core.domain.entity.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/management/kitchen")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_KITCHEN')")
public class KitchenController {

    private final KitchenService kitchenService;
    private final OrderService orderService;

    @GetMapping
    public String showKitchenPage(Model model) {
        model.addAttribute("orders_created",
                orderService.getOrdersWithStatus(OrderStatus.CREATED));
        model.addAttribute("orders_cooking",
                orderService.getOrdersWithStatus(OrderStatus.COOKING));
        return "kitchen";
    }

    @PostMapping("/start/(id)")
    public String startCooking(@PathVariable Long id) {
        kitchenService.startCooking(id);
        return "redirect:/management/kitchen";
    }

    @PostMapping("/complete/(id)")
    public String completeCooking(@PathVariable Long id) {
        kitchenService.completeCooking(id);
        return "redirect:/management/kitchen";
    }
}
