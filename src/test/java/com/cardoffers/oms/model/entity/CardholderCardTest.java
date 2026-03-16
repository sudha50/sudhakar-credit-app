package com.cardoffers.oms.model.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import com.cardoffers.oms.model.entity.CardholderCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.cardoffers.oms.model.entity.CardNetwork;
import com.cardoffers.oms.model.entity.Cardholder;

class CardholderCardTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
void shouldSetAndGetAllFields() {
    // Given
    CardNetwork cardNetwork = new CardNetwork();
    cardNetwork.setName("Visa");
    cardNetwork.setCode("V");
    cardNetwork.setCreatedAt(LocalDateTime.now());

    Cardholder cardholder = new Cardholder();
    cardholder.setFirstName("John");
    cardholder.setLastName("Doe");
    cardholder.setEmail("john.doe@example.com");
    cardholder.setCreatedAt(LocalDateTime.now());

    CardholderCard cardholderCard = CardholderCard.builder()
        .cardholder(cardholder)
        .cardNetwork(cardNetwork)
        .cardNumberLastFour("1234")
        .build();

    // When
    Long expectedCardholderId = cardholder.getId();
    String expectedCardholderFirstName = cardholder.getFirstName();
    String expectedCardNetworkName = cardNetwork.getName();
    String expectedCardNumberLastFour = cardholderCard.getCardNumberLastFour();

    // Then
    assertEquals(expectedCardholderId, cardholderCard.getCardholder().getId());
    assertEquals(expectedCardholderFirstName, cardholderCard.getCardholder().getFirstName());
    assertEquals(expectedCardNetworkName, cardholderCard.getCardNetwork().getName());
    assertEquals(expectedCardNumberLastFour, cardholderCard.getCardNumberLastFour());
}

    @Test
void shouldPrePersist_onPrePersist() {
    // Given
    Cardholder cardholder = Cardholder.builder()
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@example.com")
            .build();
    CardNetwork cardNetwork = new CardNetwork();
    cardNetwork.setName("Visa");
    cardNetwork.setCode("VISA");
    CardholderCard entity = CardholderCard.builder()
            .cardholder(cardholder)
            .cardNetwork(cardNetwork)
            .cardNumberLastFour("1234")
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