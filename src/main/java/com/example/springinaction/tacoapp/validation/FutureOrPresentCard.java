package com.example.springinaction.tacoapp.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CardExpirationValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface FutureOrPresentCard {
    String message() default "Card is expired or format is invalid. Must be MM/YY";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
