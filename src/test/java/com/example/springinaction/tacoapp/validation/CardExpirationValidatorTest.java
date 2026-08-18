package com.example.springinaction.tacoapp.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.*;

class CardExpirationValidatorTest {

    private CardExpirationValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setup() {
        validator = new CardExpirationValidator();
        context = Mockito.mock(ConstraintValidatorContext.class);
    }

    @ParameterizedTest
    @CsvSource({
            // 1. Dates is valid
            "09/26, true", "12/26, true", "02/30, true", "01/35, true",
            // 2. Dates is not valid
            "09/25, false", "10/23, false", "10/00, false", "07/26, false",
            // 3. Error formates, dates is not valid
            "8/30, false", "08-30, false", "02/2, false", "abcd, false"
    })
    @DisplayName("Check validation expiration cards")
    void shouldValidateCartExpiration(String inputDate, boolean expectedResult) {
        boolean actualResult = validator.isValid(inputDate, context);
        assertThat(actualResult)
                .as("Check card %s on valid", inputDate)
                .isEqualTo(expectedResult);
    }
}
