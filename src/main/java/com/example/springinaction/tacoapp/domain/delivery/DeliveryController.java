package com.example.springinaction.tacoapp.domain.delivery;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/management/delivery")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_DELIVERY')")
public class DeliveryController {

}
