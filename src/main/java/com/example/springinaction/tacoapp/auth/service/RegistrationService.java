package com.example.springinaction.tacoapp.auth.service;

import com.example.springinaction.tacoapp.auth.domain.UserEntity;
import com.example.springinaction.tacoapp.auth.domain.UserRole;
import com.example.springinaction.tacoapp.auth.repository.UserRepository;
import com.example.springinaction.tacoapp.auth.dto.RegistrationForm; // Твой DTO для формы
import com.example.springinaction.tacoapp.auth.dto.AdminUserForm;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerNewUser(RegistrationForm form) {
        // Защита от случайных пробелов в начале/конце
        String phone = form.getPhone().strip();
        String username = form.getUsername().strip();
        String rawPassword = form.getPassword();

        log.info("Регистрация нового пользователя: phone='{}', username='{}', passwordLength={}",
                phone, username, rawPassword.length());

        if (userRepository.findByPhone(phone).isPresent()) {
            throw new IllegalArgumentException("Пользователь с таким номером телефона уже зарегистрирован");
        }

        String encodedPassword = passwordEncoder.encode(rawPassword);
        log.debug("Пароль закодирован, хеш (первые 20 символов): {}...",
                encodedPassword.substring(0, Math.min(20, encodedPassword.length())));

        UserEntity user = UserEntity.builder()
                .name(username)
                .password(encodedPassword)
                .phone(phone)
                .role(UserRole.ROLE_USER)
                .build();

        userRepository.save(user);
        log.info("Пользователь '{}' успешно сохранён в БД с id={}", phone, user.getId());
    }

    /**
     * Создание пользователя администратором — можно указать любую роль.
     */
    @Transactional
    public void registerUserByAdmin(AdminUserForm form) {
        String phone = form.getPhone().strip();
        String username = form.getUsername().strip();

        log.info("Администратор создаёт пользователя: phone='{}', username='{}', role={}",
                phone, username, form.getRole());

        if (userRepository.findByPhone(phone).isPresent()) {
            throw new IllegalArgumentException("Пользователь с таким номером телефона уже зарегистрирован");
        }

        UserRole role;
        try {
            role = UserRole.valueOf(form.getRole());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Недопустимая роль: " + form.getRole()
                    + ". Допустимые: ROLE_USER, ROLE_DELIVERY, ROLE_KITCHEN, ROLE_DELIVERY_MANAGER, ROLE_ADMIN");
        }

        String encodedPassword = passwordEncoder.encode(form.getPassword());

        UserEntity user = UserEntity.builder()
                .name(username)
                .password(encodedPassword)
                .phone(phone)
                .role(role)
                .build();

        userRepository.save(user);
        log.info("Администратор создал пользователя id={}, phone='{}', role={}", user.getId(), phone, role);
    }
}
