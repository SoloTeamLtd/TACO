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
        String encodedPassword = passwordEncoder.encode("password");

        // 1. Массовая инициализация — только если таблица пуста
        if (userRepository.count() == 0) {
            log.info("Запуск автоматической инициализации тестовых пользователей через Java BCrypt...");

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

            log.info("Все начальные пользователи успешно созданы.");
        } else {
            log.info("Инициализация начальных пользователей пропущена: в auth_db уже есть данные.");
        }

        // 2. DeliveryManager — добавляем всегда, если его нет
        if (userRepository.findByPhone("+79990001133").isEmpty()) {
            log.info("Добавление тестового пользователя DeliveryManager...");
            userRepository.save(UserEntity.builder()
                    .name("DeliveryManager")
                    .phone("+79990001133")
                    .password(encodedPassword)
                    .role(UserRole.ROLE_DELIVERY_MANAGER)
                    .build());
            log.info("Пользователь DeliveryManager успешно добавлен.");
        } else {
            log.info("Пользователь DeliveryManager уже существует, пропускаем.");
        }
    }
}
