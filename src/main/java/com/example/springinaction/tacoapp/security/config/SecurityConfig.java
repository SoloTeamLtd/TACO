package com.example.springinaction.tacoapp.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        // Страница ввода СМС доступна ТОЛЬКО тем, кто прошел 1-й шаг (ввел верный пароль)
                        .requestMatchers("/auth/verify-sms").hasAuthority("ROLE_PRE_AUTH")

                        // Обычные доступы
                        .requestMatchers("/management/kitchen/**").hasAnyAuthority("ROLE_KITCHEN", "ROLE_ADMIN")
                        .requestMatchers("/management/delivery/**").hasAnyAuthority("ROLE_DELIVERY", "ROLE_ADMIN")
                        .requestMatchers("/design", "/orderForm", "/orders/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

                        // Открытые страницы
                        .requestMatchers("/auth/login", "/auth/register", "/css/auth.css", "/images/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("phone")
                        .passwordParameter("password")
                        // После успешного ввода ПАРОЛЯ, Spring Security кидает юзера на генерацию СМС!
                        .defaultSuccessUrl("/auth/send-sms-interceptor", true)
                )
                .logout(logout -> logout.logoutSuccessUrl("/auth/login"))
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/access-denied")
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
