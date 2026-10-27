package com.example.springinaction.tacoapp.auth.config;

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
                        // Доступ к СМС-процедурам для временной роли
                        .requestMatchers("/auth/verify-sms", "/auth/send-sms-interceptor", "/auth/access-denied").hasAuthority("ROLE_PRE_AUTH")

                        // Закрытые панели менеджеров
                        .requestMatchers("/management/kitchen/**").hasAnyAuthority("ROLE_KITCHEN", "ROLE_ADMIN")
                        .requestMatchers("/management/delivery/**").hasAnyAuthority("ROLE_DELIVERY", "ROLE_ADMIN")
                        .requestMatchers("/design", "/orderForm", "/orders/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

                        // ОТКРЫТЫЕ СТРАНИЦЫ: Добавляем сюда корень "/"
                        .requestMatchers("/", "/auth/login", "/auth/register", "/css/**", "/images/**", "/error").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/auth/register").permitAll()

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("phone")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/auth/send-sms-interceptor", true)
                )
                .logout(logout -> logout.logoutSuccessUrl("/auth/login"))
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/auth/access-denied")
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
