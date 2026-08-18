package com.example.springinaction.tacoapp.validation.com.example.springinaction.tacoapp.entity;

import com.example.springinaction.tacoapp.entity.TacoOrder;
import jakarta.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.*;
import java.util.Set;
import java.util.stream.Stream;

class TacoOrderValidationTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()){
            validator = factory.getValidator();
        }
    }

    @Test
    @DisplayName("Correct validation, all fields are filled in correctly")
    void whenAllFieldsAreValid_thenNoViolations() {
        TacoOrder order = createValidOder();
        Set<ConstraintViolation<TacoOrder>> violations = validator.validate(order);
        assertThat(violations).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("invalidOrderProvider")
    @DisplayName("Validation should be fail")
    void whenFieldIsInvalid_thenValidationFails(String propertyPath, Object invalidValue, String expectedMessage) {
        TacoOrder order = createValidOder();
        updateField(order, propertyPath, invalidValue);
        Set<ConstraintViolation<TacoOrder>> violations = validator.validate(order);
        assertThat(violations)
                .as("Validation error was expected for the field %s with value %s",
                        propertyPath, expectedMessage)
                .isNotEmpty()
                .element(0)
                .satisfies(violation -> {
                    assertThat(violation.getPropertyPath().toString()).isEqualTo(propertyPath);
                    assertThat(violation.getMessage()).isEqualTo(expectedMessage);
                });
    }

    // Helper method for valid Order
    private TacoOrder createValidOder() {
        TacoOrder order = new TacoOrder();
        order.setDeliveryName("Karl Burton");
        order.setDeliveryStreet("123 Main St");
        order.setDeliveryCity("Boston");
        order.setDeliveryState("MA");
        order.setDeliveryZip("02108");
        order.setCcNumber("4111111111111111");
        order.setCcNumber("4111111111111111");
        order.setCcExpiration("12/28");
        order.setCcCvv("123");
        return order;
    }

    // Helper method for not valid Order
    private static Stream<Arguments> invalidOrderProvider() {
        return Stream.of(
                Arguments.of("deliveryName", "", "Delivery name is required"),
                Arguments.of("deliveryStreet", null, "Delivery street is required"),
                Arguments.of("ccNumber", "1234", "Invalid credit card number"),
                Arguments.of("ccExpiration", "01/20", "Card is expired or format is invalid. Must be MM/YY"),
                Arguments.of("ccExpiration", "invalid-format", "Card is expired or format is invalid. Must be MM/YY"),
                Arguments.of("ccCvv", "1234", "Invalid CVV"),
                Arguments.of("ccCvv", "12a", "Invalid CVV")
        );
    }

    // Helper for replacing fields in a parameterized test
    private void updateField(TacoOrder order, String field, Object value) {
        switch (field) {
            case "deliveryName" -> order.setDeliveryName((String) value);
            case "deliveryStreet" -> order.setDeliveryStreet((String) value);
            case "ccNumber" -> order.setCcNumber((String) value);
            case "ccExpiration" -> order.setCcExpiration((String) value);
            case "ccCvv" -> order.setCcCvv((String) value);
        }
    }
}
