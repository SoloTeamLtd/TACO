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
@Slf4j
public class DeliveryController {

    private final DeliveryService deliveryService;

    // ─────────────────────────────────────────────
    //  Общие для всех доставщиков и менеджеров
    // ─────────────────────────────────────────────

    @GetMapping
    public String defaultDeliveryRedirect() {
        return "redirect:/management/delivery/queue";
    }

    @GetMapping("/queue")
    @PreAuthorize("hasAnyAuthority('ROLE_DELIVERY', 'ROLE_ADMIN', 'ROLE_DELIVERY_MANAGER')")
    public String showDeliveryQueue(Model model) {
        log.info("Запрос очереди заказов для доставки.");
        model.addAttribute("ready_orders", deliveryService.getOrdersReadyForDelivery());
        return "/management/delivery/delivery_queue";
    }

    @PostMapping("/start/{id}")
    @PreAuthorize("hasAuthority('ROLE_DELIVERY') or hasAuthority('ROLE_ADMIN')")
    public String takeOrder(@PathVariable Long id) {
        log.info("Заказ ID: {} взят в доставку.", id);
        deliveryService.takeOrder(id);
        return "redirect:/management/delivery/my";
    }

    // ─────────────────────────────────────────────
    //  Личный кабинет курьера (только ROLE_DELIVERY)
    // ─────────────────────────────────────────────

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('ROLE_DELIVERY') or hasAuthority('ROLE_ADMIN')")
    public String showMyDeliveries(Model model) {
        log.info("Запрос личной страницы курьера.");
        model.addAttribute("my_deliveries", deliveryService.getMyActiveDeliveries());
        return "/management/delivery/delivery_my";
    }

    @PostMapping("/complete/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_DELIVERY', 'ROLE_DELIVERY_MANAGER', 'ROLE_ADMIN')")
    public String completeDelivery(@PathVariable Long id) {
        log.info("Заказ ID: {} успешно доставлен клиенту.", id);
        deliveryService.completeDelivery(id);
        return "redirect:/management/delivery/my";
    }

    // ─────────────────────────────────────────────
    //  Панель менеджера (только ROLE_DELIVERY_MANAGER / ROLE_ADMIN)
    // ─────────────────────────────────────────────

    @GetMapping("/active")
    @PreAuthorize("hasAnyAuthority('ROLE_DELIVERY_MANAGER', 'ROLE_ADMIN')")
    public String showActiveDeliveries(Model model) {
        log.info("Запрос списка активных доставок (панель менеджера).");
        model.addAttribute("active_deliveries", deliveryService.getOrdersInDelivery());
        return "/management/delivery/delivery_active";
    }
}
