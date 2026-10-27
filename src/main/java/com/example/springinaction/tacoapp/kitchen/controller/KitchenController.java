package com.example.springinaction.tacoapp.kitchen.controller;

import com.example.springinaction.tacoapp.kitchen.service.KitchenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class KitchenController {

    private final KitchenService kitchenService;

    @GetMapping
    public String defaultKitchenRedirect() {
        return "redirect:/management/kitchen/queue";
    }

    @GetMapping("/queue")
    public String showKitchenQueue(Model model) {
        log.info("Запрос очереди входящих заказов для кухни.");
        model.addAttribute("orders_created", kitchenService.getOrdersInQueue());
        return "kitchen_queue";
    }

    @GetMapping("/active")
    public String showActiveKitchenOrders(Model model) {
        log.info("Запрос активных заказов на кухне.");
        model.addAttribute("orders_cooking", kitchenService.getActiveOrders());
        return "kitchen_active";
    }

    @PostMapping("/start/{id}")
    public String startCooking(@PathVariable Long id) {
        log.info("Кухня берет в работу заказ ID: {}", id);
        kitchenService.startCooking(id);
        return "redirect:/management/kitchen/active";
    }

    @PostMapping("/complete/{id}")
    public String completeCooking(@PathVariable Long id) {
        log.info("Кухня завершила приготовление заказа ID: {}", id);
        kitchenService.completeCooking(id);
        return "redirect:/management/kitchen/queue";
    }
}
