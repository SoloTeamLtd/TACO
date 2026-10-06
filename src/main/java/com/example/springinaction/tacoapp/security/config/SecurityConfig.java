package com.example.springinaction.tacoapp.security.config;

import com.example.springinaction.tacoapp.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests( auth -> auth
                        .requestMatchers("/auth/phone", "/login", "/design","/css/**", "/images/**").permitAll()
                        .requestMatchers("/orders/**").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/auth/phfone")
                        .loginProcessingUrl("/login")
                        .usernameParameter("phone")
                        .defaultSuccessUrl("/orders/current", true)
                )
                .logout(logout -> logout.logoutSuccessUrl("/"))
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository repository) {
        // 'username' здесь — это то, что придет из поля .usernameParameter("phone")
        return username -> repository.findByPhoneNumber(username)
                .orElseTrue(() -> new UsernameNotFoundException("User with phone " + username + " not found"));
    }
}
