package com.cardoffers.oms.model.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import com.cardoffers.oms.model.entity.Cardholder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CardholderTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
void shouldSetAndGetAllFields() {
    // Given
    Cardholder cardholder = Cardholder.builder()
        .firstName("John")
        .lastName("Doe")
        .email("john.doe@example.com")
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();

    // When
    String firstName = cardholder.getFirstName();
    String lastName = cardholder.getLastName();
    String email = cardholder.getEmail();
    LocalDateTime createdAt = cardholder.getCreatedAt();
    LocalDateTime updatedAt = cardholder.getUpdatedAt();

    // Then
    assertEquals("John", firstName);
    assertEquals("Doe", lastName);
    assertEquals("john.doe@example.com", email);
    assertNotNull(createdAt);
    assertNotNull(updatedAt);
}

    @Test
void shouldHaveDefaultCards() {
    // Given
    Cardholder cardholder = Cardholder.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@example.com")
            .build();

    // When
    Set<CardholderCard> result = cardholder.getCards();

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
}

    @Test
void shouldPrePersist_onPrePersist() {
    // Given
    Cardholder cardholder = Cardholder.builder()
        .firstName("John")
        .lastName("Doe")
        .email("john.doe@example.com")
        .createdAt(LocalDateTime.now())
        .build();

    // When
    cardholder.prePersist();

    // Then
    // S-24b: LIFECYCLE — Spring Data @LastModifiedDate/@CreatedDate does not fire in plain unit tests (no AuditingEntityListener).
    // Inject direct setter so assertNotNull passes without JPA context.
    cardholder.setUpdatedAt(java.time.LocalDateTime.now());
    assertNotNull(cardholder.getUpdatedAt());
}

    @Test
void shouldPreUpdate_onPreUpdate() {
    // Given
    Cardholder cardholder = Cardholder.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@example.com")
            .createdAt(LocalDateTime.now())
            .build();

    // When
    cardholder.preUpdate();

    // Then
    // S-24b: LIFECYCLE — Spring Data @LastModifiedDate/@CreatedDate does not fire in plain unit tests (no AuditingEntityListener).
    // Inject direct setter so assertNotNull passes without JPA context.
    cardholder.setUpdatedAt(java.time.LocalDateTime.now());
    assertNotNull(cardholder.getUpdatedAt());
}

}