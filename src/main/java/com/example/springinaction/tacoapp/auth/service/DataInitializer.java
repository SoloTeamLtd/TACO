package com.example.springinaction.tacoapp.auth.service;

import com.example.springinaction.tacoapp.auth.domain.UserEntity;
import com.example.springinaction.tacoapp.auth.domain.UserRole;
import com.example.springinaction.tacoapp.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) {
            log.info("Инициализация пользователей пропущена: в auth_db уже есть данные.");
            return;
        }

        log.info("Запуск автоматической инициализации тестовых пользователей через Java BCrypt...");

        String encodedPassword = passwordEncoder.encode("password");

        userRepository.save(UserEntity.builder()
                .name("Admin")
                .phone("+79991112233")
                .password(encodedPassword)
                .role(UserRole.ROLE_ADMIN)
                .build());

        userRepository.save(UserEntity.builder()
                .name("User")
                .phone("+79994445566")
                .password(encodedPassword)
                .role(UserRole.ROLE_USER)
                .build());

        userRepository.save(UserEntity.builder()
                .name("Chef")
                .phone("+79997778899")
                .password(encodedPassword)
                .role(UserRole.ROLE_KITCHEN)
                .build());

        userRepository.save(UserEntity.builder()
                .name("Courier")
                .phone("+79990001122")
                .password(encodedPassword)
                .role(UserRole.ROLE_DELIVERY)
                .build());

        log.info("Все тестовые пользователи успешно созданы автоматически в правильном порядке!");
    }
}
