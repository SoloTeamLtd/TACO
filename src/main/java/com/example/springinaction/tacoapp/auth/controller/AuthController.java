package com.example.springinaction.tacoapp.auth.controller;

import com.example.springinaction.tacoapp.auth.domain.UserEntity;
import com.example.springinaction.tacoapp.auth.service.RegistrationService;
import com.example.springinaction.tacoapp.auth.service.SmsCodeService;
import com.example.springinaction.tacoapp.auth.service.UserService;
import com.example.springinaction.tacoapp.auth.dto.RegistrationForm;
import com.example.springinaction.tacoapp.order.service.OrderService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@Controller
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final SmsCodeService smsCodeService;
    private final RegistrationService registrationService;
    private final UserService userService;
    private final OrderService orderService;

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegistration(@ModelAttribute("registrationForm")
                                          RegistrationForm form, Model model) {
        try {
            registrationService.registerNewUser(form);
            return "redirect:/auth/login?success";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/register";
        }
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/send-sms-interceptor")
    public String sendSmsInterceptor(Principal principal) {
        String phone = principal.getName();
        smsCodeService.sendSmsCode(phone);
        return "redirect:/auth/verify-sms";
//        return "redirect:/auth/verify-sms?debugCode=1111";
    }

    @GetMapping("/verify-sms")
    public String verifySmsPage() {
        return "auth/verify-sms";
    }

    @PostMapping("/verify-sms")
    public String processVerifySms(@RequestParam("code") String code,
                                   Principal principal,
                                   HttpServletResponse response) {
        String phone = principal.getName();
        boolean isVerified = userService.verifySmsAndUpgradeAuth(phone, code, response);
        if (isVerified) {
            UserEntity user = userService.findUserByPhone(phone);
            return switch (user.getRole()) {
                case ROLE_USER -> "redirect:/auth/dashboard";
                case ROLE_DELIVERY -> "redirect:/management/delivery/queue";
                case ROLE_DELIVERY_MANAGER -> "redirect:/management/delivery/active";
                case ROLE_KITCHEN -> "redirect:/management/kitchen";
                default -> "redirect:/";
            };
        }
        return "redirect:/auth/verify-sms?error";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "auth/access-denied";
    }

    @GetMapping("/dashboard")
    public String dashboard(Principal principal, Model model) {
        UserEntity user = userService.findUserByPhone(principal.getName());
        model.addAttribute("userName", user.getName());

        // Заказы текущего пользователя (первые 10, последние)
        Page<com.example.springinaction.tacoapp.order.domain.TacoOrder> ordersPage =
                orderService.getOrdersForCurrentUser(0, 10);
        model.addAttribute("orders", ordersPage.getContent());
        model.addAttribute("totalPages", ordersPage.getTotalPages());
        model.addAttribute("currentPage", 0);

        return "dashboard";
    }
}
