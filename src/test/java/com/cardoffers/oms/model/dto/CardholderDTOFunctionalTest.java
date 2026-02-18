package com.cardoffers.oms.model.dto;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class CardholderDTOFunctionalTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldCreateCardholderDTOWithValidFields() {
        CardholderDTO cardholder = new CardholderDTO();
        cardholder.setFirstName("John");
        cardholder.setLastName("Doe");
        cardholder.setEmail("john.doe@example.com");
        cardholder.setPhoneNumber("123-456-7890");
        cardholder.setActive(true);
        cardholder.setCardNetworks(Collections.emptyList());

        var violations = validator.validate(cardholder);
        assertTrue(violations.isEmpty(), "CardholderDTO should be valid");
    }

    @Test
    public void shouldRejectCardholderDTOWhenFirstNameIsBlank() {
        CardholderDTO cardholder = new CardholderDTO();
        cardholder.setFirstName("");
        cardholder.setLastName("Doe");
        cardholder.setEmail("john.doe@example.com");
        
        var violations = validator.validate(cardholder);
        assertFalse(violations.isEmpty(), "CardholderDTO should be invalid due to blank first name");
    }

    @Test
    public void shouldRejectCardholderDTOWhenEmailIsInvalid() {
        CardholderDTO cardholder = new CardholderDTO();
        cardholder.setFirstName("John");
        cardholder.setLastName("Doe");
        cardholder.setEmail("invalid-email");
        
        var violations = validator.validate(cardholder);
        assertFalse(violations.isEmpty(), "CardholderDTO should be invalid due to invalid email");
    }

    @Test
    public void shouldRejectCardholderDTOWhenLastNameIsBlank() {
        CardholderDTO cardholder = new CardholderDTO();
        cardholder.setFirstName("John");
        cardholder.setLastName("");
        cardholder.setEmail("john.doe@example.com");
        
        var violations = validator.validate(cardholder);
        assertFalse(violations.isEmpty(), "CardholderDTO should be invalid due to blank last name");
    }

    @Test
    public void shouldRejectCardholderDTOWhenEmailIsNull() {
        CardholderDTO cardholder = new CardholderDTO();
        cardholder.setFirstName("John");
        cardholder.setLastName("Doe");
        cardholder.setEmail(null);
        
        var violations = validator.validate(cardholder);
        assertFalse(violations.isEmpty(), "CardholderDTO should be invalid due to null email");
    }
}