package com.example.springinaction.tacoapp.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class SmsCodeService {

    private final Map<String, String> codeStorage = new ConcurrentHashMap<>();

    public void sendSmsCode(String phone) {
        String mockCode = "1111";
        codeStorage.put(phone, mockCode);

        // Используем профессиональное логирование со структурированными границами
        log.info("\n========================================\n" +
                "📱 СМС для номера {}: Код подтверждения {}\n" +
                "========================================", phone, mockCode);
    }

    public boolean verifyCode(String phone, String inputCode) {
        String savedCode = codeStorage.get(phone);
        return savedCode != null && savedCode.equals(inputCode);
    }

    public void clearCode(String phone) {
        codeStorage.remove(phone);
    }
}
