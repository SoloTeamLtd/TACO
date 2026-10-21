package com.example.springinaction.tacoapp.core.domain.service;

import com.example.springinaction.tacoapp.core.domain.entity.User;
import com.example.springinaction.tacoapp.core.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        // Возвращаем объект пользователя, но временно урезаем его права до ROLE_PRE_AUTH!
        // Это не даст ему ходить по защищенным эндпоинтам еды, пока он не введет СМС.
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getPhone())
                .password(user.getPassword()) // Здесь лежит хэш BCrypt, Spring проверит его сам
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_PRE_AUTH")))
                .build();
    }
}
