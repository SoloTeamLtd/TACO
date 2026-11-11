package com.example.springinaction.tacoapp.auth.dto;

import lombok.Data;

@Data
public class AdminUserForm {
    private String username;
    private String password;
    private String phone;
    private String role; // Одна из: ROLE_USER, ROLE_DELIVERY, ROLE_KITCHEN, ROLE_DELIVERY_MANAGER, ROLE_ADMIN
}