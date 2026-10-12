package com.example.springinaction.tacoapp.domain.kitchen;

import com.example.springinaction.tacoapp.domain.customer.OrderService;
import com.example.springinaction.tacoapp.domain.customer.entity.OrderStatus;
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
@PreAuthorize("hasRole('ROLE_KITCHEN')")
public class KitchenManagerController {

    private final KitchenOrderService kitchenOrderService;
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
        kitchenOrderService.startCooking(id);
        return "redirect:/management/kitchen";
    }

    @PostMapping("/complete/(id)")
    public String completetCooking(@PathVariable Long id) {
        kitchenOrderService.completeCooking(id);
        return "redirect:/management/kitchen";
    }
}
