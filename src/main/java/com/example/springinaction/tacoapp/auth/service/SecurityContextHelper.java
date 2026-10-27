package com.example.springinaction.tacoapp.auth.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextHelper {

    /**
     * Автоматически достает username текущего вошедшего пользователя.
     * Если запрос сделан неавторизованным пользователем (например, в тестах или при создании заказа гостем),
     * вернет "SYSTEM".
     */
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return "SYSTEM";
    }
}
