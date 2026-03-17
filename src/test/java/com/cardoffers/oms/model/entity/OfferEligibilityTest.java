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
import com.cardoffers.oms.model.entity.OfferEligibility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.cardoffers.oms.model.entity.Cardholder;
import com.cardoffers.oms.model.entity.Offer;

class OfferEligibilityTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
void shouldSetAndGetAllFields() {
    // Given
    Offer offer = new Offer();
    offer.setTitle("Special Offer");
    offer.setDescription("Exclusive offer for cardholders.");
    
    Cardholder cardholder = new Cardholder();
    cardholder.setFirstName("John");
    cardholder.setLastName("Doe");
    
    LocalDateTime checkedAt = LocalDateTime.now();
    OfferEligibility offerEligibility = OfferEligibility.builder()
            .offer(offer)
            .cardholder(cardholder)
            .isEligible(true)
            .checkedAt(checkedAt)
            .build();

    // When
    Offer resultOffer = offerEligibility.getOffer();
    Cardholder resultCardholder = offerEligibility.getCardholder();
    Boolean resultIsEligible = offerEligibility.getIsEligible();
    LocalDateTime resultCheckedAt = offerEligibility.getCheckedAt();

    // Then
    assertEquals(offer, resultOffer);
    assertEquals(cardholder, resultCardholder);
    assertTrue(resultIsEligible);
    assertEquals(checkedAt, resultCheckedAt);
}

    @Test
void shouldPrePersist_onPrePersist() {
    // Given
    Offer offer = Offer.builder().title("Special Offer").build();
    Cardholder cardholder = new Cardholder();
    cardholder.setFirstName("John");
    cardholder.setLastName("Doe");
    cardholder.setEmail("john.doe@example.com");
    OfferEligibility entity = OfferEligibility.builder()
        .offer(offer)
        .cardholder(cardholder)
        .isEligible(true)
        .checkedAt(LocalDateTime.now()).build();

    // When
    entity.prePersist();

    // Then
    assertNotNull(entity.getCheckedAt());
}

}