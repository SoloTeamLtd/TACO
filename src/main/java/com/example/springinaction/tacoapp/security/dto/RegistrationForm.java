package com.example.springinaction.tacoapp.security.dto;

import lombok.Data;

@Data
public class RegistrationForm {
    private String username;
    private String password;
    private String phone;
}