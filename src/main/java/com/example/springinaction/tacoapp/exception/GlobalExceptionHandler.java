package com.example.springinaction.tacoapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Ловим и ошибки отсутствия (404), и ошибки бизнес-логики (400)
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public String handleKitchenErrors(RuntimeException ex, RedirectAttributes redirectAttributes) {

        // FlashAttributes сохраняются в сессии только на ОДИН следующий запрос (после редиректа)
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());

        return "redirect:/management/kitchen";
    }

    @ExceptionHandler(Exception.class)
    public String handleGlobalException(Exception ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage",
                "Произошла внутренняя ошибка сервера. Пожалуйста, попробуйте позже.");
        return "redirect:/management/kitchen";
    }

    /*
    // 1. Ловим ошибки отсутствия данных (ID не найден в БД)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(IllegalArgumentException ex) {
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                null
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // 2. Ловим нарушения бизнес-логики (неверный статус заказа)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalStateException ex) {
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                null
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // 3. Ловим ошибки валидации полей (например, при отправке некорректного DTO/формы)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Ошибка валидации данных",
                errors
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // 4. Глобальный перехватчик для всего остального (чтобы не светить внутренности системы)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Произошла внутренняя ошибка сервера. Пожалуйста, попробуйте позже.",
                null
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }*/
}
