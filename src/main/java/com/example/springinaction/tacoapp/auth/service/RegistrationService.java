package com.example.springinaction.tacoapp.auth.service;

import com.example.springinaction.tacoapp.auth.domain.User;
import com.example.springinaction.tacoapp.auth.domain.UserRole;
import com.example.springinaction.tacoapp.auth.repository.UserRepository;
import com.example.springinaction.tacoapp.auth.dto.RegistrationForm; // Твой DTO для формы
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
        if (userRepository.findByPhone(form.getPhone()).isPresent()) {
            throw new IllegalArgumentException("Пользователь с таким номером телефона уже зарегистрирован");
        }

        User user = User.builder()
                .name(form.getUsername())
                .password(passwordEncoder.encode(form.getPassword()))
                .phone(form.getPhone())
                .role(UserRole.ROLE_USER)
                .build();

        userRepository.save(user);
    }
}
