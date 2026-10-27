package com.example.springinaction.tacoapp.auth.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SmsCodeService {

    private final Map<String, String> codeStorage = new ConcurrentHashMap<>();

    public void sendSmsCode(String phone) {
        // Заглушка: всегда генерируем "1111". Позже здесь будет Random
        String mockCode = "1111";

        codeStorage.put(phone, mockCode);

        // Имитируем отправку — пишем в лог бэкенда
        System.out.println("========================================");
        System.out.println("СМС для номера " + phone + ": Код подтверждения " + mockCode);
        System.out.println("========================================");
    }

    public boolean verifyCode(String phone, String inputCode) {
        String savedCode = codeStorage.get(phone);
        return savedCode != null && savedCode.equals(inputCode);
    }

    public void clearCode(String phone) {
        codeStorage.remove(phone);
    }
}
