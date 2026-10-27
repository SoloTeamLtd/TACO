package com.example.springinaction.tacoapp.delivery.controller;

import com.example.springinaction.tacoapp.delivery.service.DeliveryService;
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
@RequestMapping("/management/delivery")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_DELIVERY')")
@Slf4j
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping("/queue")
    public String showDeliveryQueue(Model model) {
        log.info("Запрос очереди заказов для доставки.");
        model.addAttribute("ready_orders", deliveryService.getOrdersReadyForDelivery());
        return "delivery_queue";
    }

    @GetMapping("/active")
    public String showActiveDeliveries(Model model) {
        log.info("Запрос списка активных доставок.");
        model.addAttribute("active_deliveries", deliveryService.getOrdersInDelivery());
        return "delivery_active";
    }

    @PostMapping("/start/{id}")
    public String takeOrder(@PathVariable Long id) {
        log.info("Заказ ID: {} взят в доставку.", id);
        deliveryService.takeOrder(id);
        return "redirect:/management/delivery/active";
    }

    @PostMapping("/complete/{id}")
    public String completeDelivery(@PathVariable Long id) {
        log.info("Заказ ID: {} успешно доставлен клиенту.", id);
        deliveryService.completeDelivery(id);
        return "redirect:/management/delivery/queue";
    }
}
