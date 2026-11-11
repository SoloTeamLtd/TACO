package com.example.springinaction.tacoapp.auth.service;

import com.example.springinaction.tacoapp.auth.domain.UserEntity;
import com.example.springinaction.tacoapp.auth.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final SmsCodeService smsCodeService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByPhone(phone)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        log.debug("loadUserByUsername: phone='{}', найден пользователь id={}, role={}",
                phone, userEntity.getId(), userEntity.getRole());
        log.debug("Хеш пароля из БД (первые 20 символов): {}...",
                userEntity.getPassword().substring(0, Math.min(20, userEntity.getPassword().length())));

        return User.builder()
                .username(userEntity.getPhone())
                .password(userEntity.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_PRE_AUTH")))
                .build();
    }

    public boolean verifySmsAndUpgradeAuth(String phone, String code, HttpServletResponse response) {
        if (!smsCodeService.verifyCode(phone, code)) {
            return false;
        }

    smsCodeService.clearCode(phone);
    UserEntity realUser = userRepository.findByPhone(phone)
            .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден после верификации"));

        SecurityContextHolder.clearContext();

        String jwtToken = jwtTokenProvider.generateToken(realUser);
        Cookie jwtCookie = new Cookie("JWT_TOKEN", jwtToken);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false); // Включи true, если на продакшене будет HTTPS
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(24 * 60 * 60); // 24 часа в секундах
        response.addCookie(jwtCookie);

        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                realUser,
                null,
                List.of(new SimpleGrantedAuthority(realUser.getRole().name()))
        );
        SecurityContextHolder.getContext().setAuthentication(newAuth);
        return true;
    }

    public UserEntity findUserByPhone(String phone) {
        return userRepository.findByPhone(phone)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + phone));
    }
}
