package com.example.springinaction.tacoapp.core.domain.service;

import com.example.springinaction.tacoapp.core.domain.entity.auth.User;
import com.example.springinaction.tacoapp.core.domain.repository.auth.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final SmsCodeService smsCodeService;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getPhone())
                .password(user.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_PRE_AUTH")))
                .build();
    }

    public boolean verifySmsAndUpgradeAuth(String phone, String code) {
        if (!smsCodeService.verifyCode(phone, code)) {
            return false;
        }

        smsCodeService.clearCode(phone);
        User realUser = userRepository.findByPhone(phone)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден после верификации"));
        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                realUser,
                null,
                List.of(new SimpleGrantedAuthority(realUser.getRole().name()))
        );
        SecurityContextHolder.getContext().setAuthentication(newAuth);
        return true;
    }
}
