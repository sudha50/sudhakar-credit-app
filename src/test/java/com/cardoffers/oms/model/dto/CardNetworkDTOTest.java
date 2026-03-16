package com.cardoffers.oms.model.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import jakarta.validation.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import com.cardoffers.oms.model.dto.CardNetworkDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CardNetworkDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
void shouldSetAndGetAllFields() {
    // Given
    CardNetworkDTO cardNetworkDTO = CardNetworkDTO.builder()
            .name("Visa")
            .code("VISA")
            .active(true)
            .build();

    // When
    String name = cardNetworkDTO.getName();
    String code = cardNetworkDTO.getCode();
    Boolean active = cardNetworkDTO.getActive();

    // Then
    assertEquals("Visa", name);
    assertEquals("VISA", code);
    assertEquals(true, active);
}

    @Test
void shouldPassValidation_withValidFields() {
    // Given
    CardNetworkDTO dto = CardNetworkDTO.builder()
                                        .name("Visa")
                                        .code("VISA")
                                        .active(true)
                                        .build();
    
    // When
    Set<ConstraintViolation<CardNetworkDTO>> violations = validator.validate(dto);

    // Then
    assertEquals(0, violations.size());
}

    @Test
void shouldFailValidation_withNullRequiredFields() {
    // Given
    CardNetworkDTO dto = CardNetworkDTO.builder()
        .name(null)
        .code(null)
        .active(true)
        .build();
    
    // When
    Set<ConstraintViolation<CardNetworkDTO>> violations = validator.validate(dto);
    
    // Then
    assertFalse(violations.isEmpty());
}

}