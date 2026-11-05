package com.example.springinaction.tacoapp.auth.controller;

import com.example.springinaction.tacoapp.auth.domain.UserEntity;
import com.example.springinaction.tacoapp.auth.service.RegistrationService;
import com.example.springinaction.tacoapp.auth.service.SmsCodeService;
import com.example.springinaction.tacoapp.auth.service.UserService;
import com.example.springinaction.tacoapp.auth.dto.RegistrationForm;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
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
//        return "redirect:/auth/verify-sms";
        return "redirect:/auth/verify-sms?debugCode=1111";
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
            return "redirect:/orders/my";
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
        return "auth/dashboard";
    }
}
