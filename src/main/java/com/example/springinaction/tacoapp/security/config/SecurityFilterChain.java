package com.example.springinaction.tacoapp.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .authorizeHttpRequests(auth -> auth
                    // Страницы кухни и доставки доступны только соответствующим ролям и админу
                    .requestMatchers("/management/kitchen/**").hasAnyRole("KITCHEN", "ADMIN")
                    .requestMatchers("/management/delivery/**").hasAnyRole("DELIVERY", "ADMIN")

                    // Страницы заказа и создания дизайна доступны обычным пользователям и админу
                    .requestMatchers("/design", "/orderForm").hasAnyRole("USER", "ADMIN")

                    // Главная страница доступна всем авторизованным пользователям
                    .requestMatchers("/home", "/").authenticated()

                    .anyRequest().permitAll()
            )
            // Настройка формы логина
            .formLogin(form -> form
                    .defaultSuccessUrl("/home", true)
            );

    return http.build();
}