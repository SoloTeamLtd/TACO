package com.example.springinaction.tacoapp.auth.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

// оставляем на rest
@Component
@AllArgsConstructor
public class JWTFilter /*extends OncePerRequestFilter*/ {
/*
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = null;

        // Ищем нашу JWT куку в запросе
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt-token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // Если токен нашли, пытаемся его валидировать
        if (token != null && !token.isBlank()) {
            try {
                Map<String, String> claims = jwtUtil.validateTokenAndRetrieveClaims(token);
                String phone = claims.get("phone");
                String role = claims.get("role");

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    // Создаем объект аутентификации для Spring Security на основе данных из токена
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    phone,
                                    null,
                                    List.of(new SimpleGrantedAuthority(role)) // Выдаем роль, которая зашита в JWT
                            );

                    // Пропускной выдан — теперь Spring знает, кто это
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (JWTVerificationException exc) {
                // Токен «протух» или подделан — можно очистить куку или просто заигнорить,
                // тогда Spring Security вернет 403/401 для защищенных эндпоинтов
                Cookie clearCookie = new Cookie("jwt-token", null);
                clearCookie.setPath("/");
                clearCookie.setMaxAge(0);
                response.addCookie(clearCookie);
            }
        }

        // Передаем запрос дальше по цепочке фильтров
        filterChain.doFilter(request, response);
    }*/
}
