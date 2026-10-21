package com.example.springinaction.tacoapp.security;

import com.example.springinaction.tacoapp.core.domain.entity.User;
import com.example.springinaction.tacoapp.core.domain.repository.UserRepository;
import com.example.springinaction.tacoapp.core.domain.service.RegistrationService;
import com.example.springinaction.tacoapp.core.domain.service.SmsCodeService;
import com.example.springinaction.tacoapp.security.dto.RegistrationForm;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final SmsCodeService smsCodeService;
    private final UserRepository userRepository;
    private final RegistrationService registrationService;

    @PostMapping("/register") // Обрабатывает отправку формы из register.html
    public String processRegistration(@ModelAttribute("registrationForm") RegistrationForm form, Model model) {
        try {
            // ВОТ ЗДЕСЬ МЫ ЕГО ИСПОЛЬЗУЕМ!
            registrationService.registerNewUser(form);

            return "redirect:/auth/login?success"; // Успешно -> на логин
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register"; // Ошибка -> показываем ту же страницу с текстом ошибки
        }
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Форма ввода телефона и пароля
    }

    // Сюда Spring Security редиректит юзера СРАЗУ после верного ввода пароля

    @GetMapping("/send-sms-interceptor")
    public String sendSmsInterceptor(Principal principal) {
        String phone = principal.getName(); // Достаем телефон текущего полу-авторизованного юзера

        // Генерируем и шлем СМС-заглушку ("1111")
        smsCodeService.sendSmsCode(phone);

        return "redirect:/auth/verify-sms";
    }
    // Страница ввода кода СМС

    @GetMapping("/verify-sms")
    public String verifySmsPage() {
        return "verify-sms"; // Шаблон с инпутом для 4 цифр кодом
    }
    // Проверка СМС кода

    @PostMapping("/verify-sms")
    public String processVerifySms(@RequestParam("code") String code, Principal principal) {
        String phone = principal.getName();

        if (smsCodeService.verifyCode(phone, code)) {
            smsCodeService.clearCode(phone);

            // СМС верный! Достаем из БД реального пользователя с его настоящей ролью (например, ROLE_ADMIN)
            User realUser = userRepository.findByPhone(phone).orElseThrow();

            // Создаем новый полноценный токен авторизации с РЕАЛЬНЫМИ ролями юзера
            Authentication newAuth = new UsernamePasswordAuthenticationToken(
                    realUser,
                    null,
                    List.of(new SimpleGrantedAuthority(realUser.getRole().name()))
            );

            // Перезаписываем техническую роль ROLE_PRE_AUTH в контексте Spring на реальные права!
            SecurityContextHolder.getContext().setAuthentication(newAuth);

            // Всё, теперь он полноценный пользователь, пускаем в систему!
            return "redirect:/orders/current";
        }

        // Если код неверный, кидаем обратно с ошибкой
        return "redirect:/auth/verify-sms?error";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
}
