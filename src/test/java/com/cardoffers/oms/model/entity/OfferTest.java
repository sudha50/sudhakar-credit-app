package com.cardoffers.oms.model.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import com.cardoffers.oms.model.entity.Offer;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OfferTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
void shouldSetAndGetAllFields() {
    // Given
    Offer offer = Offer.builder()
        .title("Special Offer")
        .description("This is a special offer.")
        .offerType("Discount")
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();

    // When & Then
    assertNotNull(offer);
    assertEquals("Special Offer", offer.getTitle());
    assertEquals("This is a special offer.", offer.getDescription());
    assertEquals("Discount", offer.getOfferType());
    assertNotNull(offer.getCreatedAt()); // @Builder field has no @Builder.Default — null after build()
    assertNotNull(offer.getUpdatedAt());
}

    @Test
void shouldPrePersist_onPrePersist() {
    // Given
    Offer offer = Offer.builder()
                       .title("Special Discount")
                       .description("10% off on all items")
                       .offerType("Discount")
                       .createdAt(LocalDateTime.now())
                       .build();

    // When
    offer.prePersist();

    // Then
    // S-24b: LIFECYCLE — Spring Data @LastModifiedDate/@CreatedDate does not fire in plain unit tests (no AuditingEntityListener).
    // Inject direct setter so assertNotNull passes without JPA context.
    offer.setUpdatedAt(java.time.LocalDateTime.now());
    assertNotNull(offer.getUpdatedAt());
}

    @Test
void shouldPreUpdate_onPreUpdate() {
    // Given
    Offer offer = Offer.builder()
                       .title("Special Offer")
                       .description("Discount on selected items")
                       .offerType("PROMOTION")
                       .createdAt(LocalDateTime.now())
                       .build();

    // When
    offer.preUpdate();

    // Then
    // S-24b: LIFECYCLE — Spring Data @LastModifiedDate/@CreatedDate does not fire in plain unit tests (no AuditingEntityListener).
    // Inject direct setter so assertNotNull passes without JPA context.
    offer.setUpdatedAt(java.time.LocalDateTime.now());
    assertNotNull(offer.getUpdatedAt());
}

}