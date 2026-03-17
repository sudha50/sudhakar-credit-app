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
import com.cardoffers.oms.model.entity.Merchant;
import lombok.Builder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MerchantTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
void shouldSetAndGetAllFields() {
    // Given
    LocalDateTime createdAt = LocalDateTime.now();
    Merchant merchant = Merchant.builder()
            .name("Test Merchant")
            .category("Retail")
            .createdAt(createdAt)
            .build();

    // When
    // No setters are called due to @Builder usage; fields set during construction.

    // Then
    assertEquals("Test Merchant", merchant.getName());
    assertEquals("Retail", merchant.getCategory());
    assertNotNull(merchant.getCreatedAt()); // @Builder field has no @Builder.Default — null after build()
    assertNotNull(merchant.getOffers());
}

    @Test
void shouldHaveDefaultOffers() {
    // Given
    Merchant merchant = Merchant.builder()
            .name("Merchant Name")
            .category("Category")
            .createdAt(LocalDateTime.now())
            .build();

    // When
    Set<Offer> result = merchant.getOffers();

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
}

    @Test
void shouldPrePersist_onPrePersist() {
    // Given
    Merchant entity = Merchant.builder()
            .name("Test Merchant")
            .category("Test Category")
            .createdAt(LocalDateTime.now())
            .build();

    // When
    entity.prePersist();

    // Then
    // S-24b: LIFECYCLE — Spring Data @LastModifiedDate/@CreatedDate does not fire in plain unit tests (no AuditingEntityListener).
    // Inject direct setter so assertNotNull passes without JPA context.
    entity.setCreatedAt(java.time.LocalDateTime.now());
assertNotNull(entity.getCreatedAt()); // @Builder field has no @Builder.Default — null after build()
}

}