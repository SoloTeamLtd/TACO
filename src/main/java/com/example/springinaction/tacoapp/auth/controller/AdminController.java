package com.example.springinaction.tacoapp.auth.controller;

import com.example.springinaction.tacoapp.auth.dto.AdminUserForm;
import com.example.springinaction.tacoapp.auth.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/management/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final RegistrationService registrationService;

    @GetMapping
    public String adminPanel() {
        return "management/admin/admin_panel";
    }

    @GetMapping("/users/new")
    public String showCreateUserForm(Model model) {
        model.addAttribute("adminUserForm", new AdminUserForm());
        return "management/admin/user_create";
    }

    @PostMapping("/users/new")
    public String createUser(AdminUserForm form, Model model) {
        try {
            registrationService.registerUserByAdmin(form);
            log.info("Администратор создал пользователя: phone={}, role={}", form.getPhone(), form.getRole());
            return "redirect:/management/admin?created";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("adminUserForm", form);
            return "management/admin/user_create";
        }
    }
}