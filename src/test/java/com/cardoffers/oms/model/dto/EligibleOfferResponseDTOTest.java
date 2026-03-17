package com.cardoffers.oms.model.dto;

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
import com.cardoffers.oms.model.dto.EligibleOfferResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.cardoffers.oms.model.dto.OfferDTO;

class EligibleOfferResponseDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
void shouldSetAndGetAllFields() {
    // Given
    OfferDTO offer = new OfferDTO();
    offer.setTitle("Special Offer");
    offer.setOfferType("Discount");
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(10));
    offer.setSource("Online");

    List<OfferDTO> offers = new ArrayList<>();
    offers.add(offer);

    EligibleOfferResponseDTO responseDTO = EligibleOfferResponseDTO.builder()
            .cardholderId(123L)
            .cardholderName("John Doe")
            .eligibleOffers(offers)
            .build();

    // When & Then
    assertEquals(123L, responseDTO.getCardholderId());
    assertEquals("John Doe", responseDTO.getCardholderName());
    assertEquals(offers, responseDTO.getEligibleOffers());
}

    @Test
void shouldHaveDefaultEligibleOffers() {
    // Given
    EligibleOfferResponseDTO instance = EligibleOfferResponseDTO.builder()
            .cardholderId(1L)
            .cardholderName("John Doe")
            .eligibleOffers(new ArrayList<>())
            .build();

    // When
    List<OfferDTO> result = instance.getEligibleOffers();

    // Then
    assertNotNull(result);
    assertEquals(0, result.size());
}

}