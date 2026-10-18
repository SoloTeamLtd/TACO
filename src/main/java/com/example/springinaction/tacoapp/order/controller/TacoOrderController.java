package com.example.springinaction.tacoapp.order.controller;

import com.example.springinaction.tacoapp.core.domain.entity.CustomerStatus;
import com.example.springinaction.tacoapp.core.domain.entity.OrderStatus;
import com.example.springinaction.tacoapp.core.domain.entity.TacoOrder;
import com.example.springinaction.tacoapp.order.repository.TacoOrderRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/orders")
@SessionAttributes("tacoOrder")
public class TacoOrderController {

    private final TacoOrderRepository tacoOrderRepository;

    @ModelAttribute(name = "tacoOrder")
    public TacoOrder order() {
        return new TacoOrder();
    }

    @GetMapping("/current")
    public String orderForm() {
        return "orderForm";
    }

    @PostMapping
    public String processOrder(@Valid TacoOrder tacoOrder, Errors errors,
                               SessionStatus sessionStatus) {
        if (errors.hasErrors()) {
            return "orderForm";
        }

        tacoOrderRepository.save(tacoOrder);
        log.info("Order submitted: {}", tacoOrder);
        sessionStatus.setComplete();
        return "redirect:/";
    }

    public CustomerStatus mapToCustomerStatus(OrderStatus internalStatus) {
        return switch (internalStatus) {
            case CREATED, COOKING, READY -> CustomerStatus.PREPARING;
            case DELIVERING -> CustomerStatus.SHIPPED;
            case DELIVERED -> CustomerStatus.ARRIVED;
            case CANCELLED -> CustomerStatus.CANCELLED;
        };
    }
}
