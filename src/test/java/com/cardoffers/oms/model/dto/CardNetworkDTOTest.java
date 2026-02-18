package com.cardoffers.oms.model.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CardNetworkDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateCardNetworkDTO_whenAllFieldsAreValid() {
        CardNetworkDTO dto = new CardNetworkDTO();
        dto.setId(1L);
        dto.setName("Visa");
        dto.setCode("VISA");
        dto.setActive(true);

        Set<ConstraintViolation<CardNetworkDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldNotCreateCardNetworkDTO_whenNameIsBlank() {
        CardNetworkDTO dto = new CardNetworkDTO();
        dto.setId(1L);
        dto.setName("");
        dto.setCode("VISA");
        dto.setActive(true);

        Set<ConstraintViolation<CardNetworkDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("must not be blank", violations.iterator().next().getMessage());
    }

    @Test
    void shouldNotCreateCardNetworkDTO_whenCodeIsBlank() {
        CardNetworkDTO dto = new CardNetworkDTO();
        dto.setId(1L);
        dto.setName("Visa");
        dto.setCode("");
        dto.setActive(true);

        Set<ConstraintViolation<CardNetworkDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("must not be blank", violations.iterator().next().getMessage());
    }

    @Test
    void shouldCreateCardNetworkDTO_whenActiveIsNull() {
        CardNetworkDTO dto = new CardNetworkDTO();
        dto.setId(1L);
        dto.setName("Visa");
        dto.setCode("VISA");
        dto.setActive(null);

        Set<ConstraintViolation<CardNetworkDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldNotCreateCardNetworkDTO_whenAllFieldsAreNull() {
        CardNetworkDTO dto = new CardNetworkDTO();

        Set<ConstraintViolation<CardNetworkDTO>> violations = validator.validate(dto);
        assertEquals(3, violations.size());
    }
}