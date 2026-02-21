package com.cardoffers.oms.model.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

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
    public void shouldCreateCardholderDTOSuccessfully() {
        CardholderDTO cardholderDTO = new CardholderDTO();
        cardholderDTO.setFirstName("John");
        cardholderDTO.setLastName("Doe");
        cardholderDTO.setEmail("john.doe@example.com");
        cardholderDTO.setPhoneNumber("1234567890");
        cardholderDTO.setActive(true);
        cardholderDTO.setCardNetworks(List.of(new CardNetworkDTO()));

        var violations = validator.validate(cardholderDTO);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void shouldFailWhenFirstNameIsBlank() {
        CardholderDTO cardholderDTO = new CardholderDTO();
        cardholderDTO.setFirstName("");
        cardholderDTO.setLastName("Doe");
        cardholderDTO.setEmail("john.doe@example.com");

        var violations = validator.validate(cardholderDTO);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("must not be blank", violations.iterator().next().getMessage());
    }

    @Test
    public void shouldFailWhenEmailIsInvalid() {
        CardholderDTO cardholderDTO = new CardholderDTO();
        cardholderDTO.setFirstName("John");
        cardholderDTO.setLastName("Doe");
        cardholderDTO.setEmail("invalid-email");

        var violations = validator.validate(cardholderDTO);
        assertFalse(violations.isEmpty());
        assertEquals("must be a well-formed email address", violations.iterator().next().getMessage());
    }

    @Test
    public void shouldFailWhenLastNameIsBlank() {
        CardholderDTO cardholderDTO = new CardholderDTO();
        cardholderDTO.setFirstName("John");
        cardholderDTO.setLastName("");

        var violations = validator.validate(cardholderDTO);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("must not be blank", violations.iterator().next().getMessage());
    }
}