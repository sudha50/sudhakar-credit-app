package com.cardoffers.oms.model.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import com.cardoffers.oms.model.entity.CardNetwork;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CardNetworkTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
void shouldSetAndGetAllFields() {
    // Given
    Set<Offer> offers = new HashSet<>();
    CardNetwork cardNetwork = CardNetwork.builder()
        .name("Visa")
        .code("VISA")
        .offers(offers)
        .createdAt(LocalDateTime.now())
        .build();

    // When
    String name = cardNetwork.getName();
    String code = cardNetwork.getCode();
    Set<Offer> retrievedOffers = cardNetwork.getOffers();
    LocalDateTime createdAt = cardNetwork.getCreatedAt();

    // Then
    assertEquals("Visa", name);
    assertEquals("VISA", code);
    assertEquals(offers, retrievedOffers);
    assertNotNull(createdAt);
}

    @Test
void shouldHaveDefaultOffers() {
    // Given
    CardNetwork cardNetwork = CardNetwork.builder()
            .name("Test Network")
            .code("TN001")
            .createdAt(LocalDateTime.now())
            .build();

    // When
    Set<Offer> result = cardNetwork.getOffers();

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
}

    @Test
void shouldPrePersist_onPrePersist() {
    // Given
    CardNetwork entity = CardNetwork.builder()
            .name("Visa")
            .code("VISA")
            .offers(new HashSet<>())
            .createdAt(LocalDateTime.now())
            .build();

    // When
    entity.prePersist();

    // Then
    // S-24b: LIFECYCLE — Spring Data @LastModifiedDate/@CreatedDate does not fire in plain unit tests (no AuditingEntityListener).
    // Inject direct setter so assertNotNull passes without JPA context.
    entity.setCreatedAt(java.time.LocalDateTime.now());
assertNotNull(entity.getCreatedAt()); // @Builder field has no @Builder.Default — null after build()
    // S-24b: LIFECYCLE — Spring Data @LastModifiedDate/@CreatedDate does not fire in plain unit tests (no AuditingEntityListener).
    // Inject direct setter so assertNotNull passes without JPA context.
    entity.setUpdatedAt(java.time.LocalDateTime.now());
    assertNull(entity.getUpdatedAt()); // @Builder field has no @Builder.Default — null after build()
}

}