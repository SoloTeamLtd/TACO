package com.example.springinaction.tacoapp.core.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users") // "user" — зарезервированное слово в PostgreSQL, поэтому пишем "users"
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Имя пользователя для отображения в интерфейсе Thymeleaf (например, "Алексей")
    private String name;

    // Наш главный уникальный идентификатор (логин), по которому ищем в БД
    @Column(unique = true, nullable = false)
    private String phone;

    // Здесь будет храниться хэш пароля, сгенерированный через BCryptPasswordEncoder
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    /**
     * Говорим Spring Security, что уникальным логином (username) для авторизации
     * в нашем приложении является номер телефона.
     */
    @Override
    public String getUsername() {
        return this.phone;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Пароль/токен не просрочены
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
