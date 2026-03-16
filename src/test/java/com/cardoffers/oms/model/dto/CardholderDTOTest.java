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
import com.cardoffers.oms.model.dto.CardholderDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CardholderDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
void shouldSetAndGetAllFields() {
    // Given
    CardholderDTO cardholderDTO = CardholderDTO.builder()
        .firstName("John")
        .lastName("Doe")
        .email("john.doe@example.com")
        .build();

    // When
    String firstName = cardholderDTO.getFirstName();
    String lastName = cardholderDTO.getLastName();
    String email = cardholderDTO.getEmail();

    // Then
    assertEquals("John", firstName);
    assertEquals("Doe", lastName);
    assertEquals("john.doe@example.com", email);
}

    @Test
void shouldPassValidation_withValidFields() {
    // Given
    CardholderDTO dto = CardholderDTO.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@example.com")
            .build();

    // When
    Set<ConstraintViolation<CardholderDTO>> violations = validator.validate(dto);

    // Then
    assertEquals(0, violations.size());
}

    @Test
void shouldFailValidation_withNullRequiredFields() {
    // Given
    CardholderDTO dto = CardholderDTO.builder()
            .firstName(null) // Required field
            .lastName(null)  // Required field
            .email(null)     // Required field
            .build();

    // When
    Set<ConstraintViolation<CardholderDTO>> violations = validator.validate(dto);

    // Then
    assertFalse(violations.isEmpty());
}

}