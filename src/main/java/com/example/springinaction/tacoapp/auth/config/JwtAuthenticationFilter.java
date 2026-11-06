package com.example.springinaction.tacoapp.auth.config;

import com.example.springinaction.tacoapp.auth.domain.UserEntity;
import com.example.springinaction.tacoapp.auth.domain.UserRole;
import com.example.springinaction.tacoapp.auth.service.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.equals("/auth/login") ||
               path.equals("/login") ||
               path.equals("/auth/register") ||
               path.contains("/send-sms-interceptor") ||
               path.contains("/verify-sms") ||
               path.startsWith("/css/") ||
               path.startsWith("/images/") ||
               path.equals("/error");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("JWT_TOKEN".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token != null && tokenProvider.validateToken(token)) {
            String phone = tokenProvider.getUsernameFromToken(token);
            Long userId = tokenProvider.getUserIdFromToken(token);
            String role = tokenProvider.getRoleFromToken(token);
            String name = tokenProvider.getNameFromToken(token); // <--- Достаем имя из токена!

            UserEntity principalUser = UserEntity.builder()
                    .id(userId)
                    .phone(phone)
                    .name(name)
                    .role(UserRole.valueOf(role))
                    .build();

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    principalUser,
                    null,
                    List.of(new SimpleGrantedAuthority(role))
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
