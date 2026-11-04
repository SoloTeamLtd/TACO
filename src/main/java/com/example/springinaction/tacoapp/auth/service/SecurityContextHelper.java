package com.example.springinaction.tacoapp.auth.service;

import com.example.springinaction.tacoapp.auth.domain.UserEntity;
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

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Пользователь не авторизован");
        }

        Object principal = authentication.getPrincipal();

        // Вариант А: Если при валидации JWT ты кладешь в контекст доменную сущность UserEntity
        if (principal instanceof UserEntity) {
            return ((UserEntity) principal).getId();
        }

        // Вариант Б: Если в principal лежит строка с ID (часто практикуется при парсинге JWT в микросервисах)
        try {
            return Long.parseLong(principal.toString());
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Не удалось получить ID пользователя из токена", e);
        }
    }
}
