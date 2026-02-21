package com.cardoffers.oms.model.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    void shouldNotBeValid_whenNameIsBlank() {
        CardNetworkDTO dto = new CardNetworkDTO();
        dto.setCode("VISA");
        dto.setActive(true);
        
        Set<ConstraintViolation<CardNetworkDTO>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("must not be blank", violations.iterator().next().getMessage());
    }

    @Test
    void shouldNotBeValid_whenCodeIsBlank() {
        CardNetworkDTO dto = new CardNetworkDTO();
        dto.setName("Visa");
        dto.setActive(true);

        Set<ConstraintViolation<CardNetworkDTO>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("must not be blank", violations.iterator().next().getMessage());
    }

    @Test
    void shouldBeValid_whenAllFieldsAreSet() {
        CardNetworkDTO dto = new CardNetworkDTO();
        dto.setName("Visa");
        dto.setCode("VISA");
        dto.setActive(true);
        
        Set<ConstraintViolation<CardNetworkDTO>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldBeValid_whenActiveIsNull() {
        CardNetworkDTO dto = new CardNetworkDTO();
        dto.setName("MasterCard");
        dto.setCode("MC");
        dto.setActive(null);
        
        Set<ConstraintViolation<CardNetworkDTO>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldCreateDTO_whenBuilderIsUsed() {
        CardNetworkDTO dto = CardNetworkDTO.builder()
                .name("America Express")
                .code("AMEX")
                .active(true)
                .build();
        
        assertNotNull(dto);
        assertEquals("America Express", dto.getName());
        assertEquals("AMEX", dto.getCode());
        assertTrue(dto.getActive());
    }
}