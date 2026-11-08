package com.example.springinaction.tacoapp.order.controller;

import com.example.springinaction.tacoapp.auth.service.SecurityContextHelper;
import com.example.springinaction.tacoapp.order.domain.TacoOrder;
import com.example.springinaction.tacoapp.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/orders")
@SessionAttributes("tacoOrder")
public class TacoOrderController {

    private final OrderService orderService;
    private final SecurityContextHelper securityContextHelper; // Нужен для вывода имени на странице

    @ModelAttribute(name = "tacoOrder")
    public TacoOrder order() {
        return new TacoOrder();
    }

    @GetMapping("/current")
    public String orderForm() {
        return "orderForm";
    }

    @GetMapping("/my")
    public String userOrders(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "5") int size,
                             Model model) {
        log.info("Отрисовка личного кабинета заказов.");

        Page<TacoOrder> orderPage = orderService.getOrdersForCurrentUser(page, size);

        // Передаем динамические данные в Thymeleaf
        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("userName", securityContextHelper.getCurrentName()); // Передаем имя пользователя

        return "orders/userOrders"; // Путь к файлу шаблона: templates/orders/userOrders.html
    }


    @PostMapping
    public String processOrder(@Valid TacoOrder tacoOrder, Errors errors,
                               SessionStatus sessionStatus) {
        if (errors.hasErrors()) {
            return "orderForm";
        }

        orderService.createOrder(tacoOrder);
        sessionStatus.setComplete();
        return "redirect:/";
    }
}
