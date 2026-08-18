package com.example.springinaction.tacoapp.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.regex.Pattern;

public class CardExpirationValidator implements ConstraintValidator<FutureOrPresentCard, String> {

    private static final Pattern FORMAT_PATTERN = Pattern.compile("^(0[1-9]|1[0-2])/([0-9]{2})$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        if (!FORMAT_PATTERN.matcher(value).matches()) {
            return false;
        }

            // 1. Check syntax

        try {
            // 2. Parse month, year
            String[] parts = value.split("/");
            int month = Integer.parseInt(parts[0]);
            int year = 2000 + Integer.parseInt(parts[1]);
            YearMonth cardExpire = YearMonth.of(year, month);

            // 3. Current year in absolute time (UTC)
            YearMonth currentMonthUts = YearMonth.now(ZoneOffset.UTC);

            return !cardExpire.isBefore(currentMonthUts);
        } catch (Exception e) {
            return false;
        }
    }
}
