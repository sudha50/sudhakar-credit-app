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
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import com.cardoffers.oms.model.dto.MerchantDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MerchantDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
void shouldSetAndGetAllFields() {
    // Given
    MerchantDTO dto = MerchantDTO.builder()
            .name("Test Merchant")
            .category("Retail")
            .description("A test merchant description")
            .build();

    // When
    String name = dto.getName();
    String category = dto.getCategory();
    String description = dto.getDescription();

    // Then
    assertEquals("Test Merchant", name);
    assertEquals("Retail", category);
    assertEquals("A test merchant description", description);
}

    @Test
void shouldPassValidation_withValidFields() {
    // Given
    MerchantDTO dto = MerchantDTO.builder()
            .name("Valid Name")
            .description("Valid Description")
            .category("Valid Category")
            .build();
    
    // When
    Set<ConstraintViolation<MerchantDTO>> violations = validator.validate(dto);

    // Then
    assertEquals(0, violations.size());
}

    @Test
void shouldFailValidation_withNullRequiredFields() {
    // Given
    MerchantDTO dto = MerchantDTO.builder()
            .name(null)
            .category(null)
            .build();
    
    // When
    Set<ConstraintViolation<MerchantDTO>> violations = validator.validate(dto);
    
    // Then
    assertFalse(violations.isEmpty());
}

}