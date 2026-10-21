package com.example.springinaction.tacoapp.core.domain.service;

import com.example.springinaction.tacoapp.core.domain.entity.User;
import com.example.springinaction.tacoapp.core.domain.entity.UserRole;
import com.example.springinaction.tacoapp.core.domain.repository.UserRepository;
import com.example.springinaction.tacoapp.security.dto.RegistrationForm; // Твой DTO для формы
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerNewUser(RegistrationForm form) {
        // Проверяем, нет ли уже пользователя с таким логином/телефоном
        if (userRepository.findByPhone(form.getPhone()).isPresent()) {
            throw new IllegalArgumentException("Пользователь с таким номером телефона уже зарегистрирован");
        }

        // Хэшируем пароль и собираем сущность
        User user = User.builder()
                .name(form.getUsername()) // Имя пользователя для отображения
                .password(passwordEncoder.encode(form.getPassword())) // Хэшируем!
                .phone(form.getPhone()) // Телефон, по которому будем логиниться
                .role(UserRole.ROLE_USER)     // По умолчанию даем роль обычного пользователя
                .build();

        userRepository.save(user);
    }
}
