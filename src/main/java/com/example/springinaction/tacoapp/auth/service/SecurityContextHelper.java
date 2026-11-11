package com.example.springinaction.tacoapp.auth.service;

import com.example.springinaction.tacoapp.auth.domain.UserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextHelper {

    /**
     * Автоматически достает username (телефон) текущего вошедшего пользователя.
     * Если запрос сделан неавторизованным пользователем (например, в тестах или при создании заказа гостем),
     * вернет "SYSTEM".
     */
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserEntity) {
                return ((UserEntity) principal).getUsername(); // телефон
            }
            return authentication.getName();
        }
        return "SYSTEM";
    }

    /**
     * Достает имя (name) текущего вошедшего пользователя из UserEntity в SecurityContext.
     * Если principal — строка (не UserEntity), возвращается как есть.
     * Если пользователь не авторизован, возвращается "Гость".
     */
    public String getCurrentName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserEntity) {
                return ((UserEntity) principal).getName();
            }
            // fallback: если principal — строка (например, телефон)
            return principal.toString();
        }
        return "Гость";
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
