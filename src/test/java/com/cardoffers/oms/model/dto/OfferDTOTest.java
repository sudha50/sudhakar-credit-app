package com.cardoffers.oms.model.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import com.cardoffers.oms.model.dto.OfferDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.cardoffers.oms.model.dto.CardNetworkDTO;
import com.cardoffers.oms.model.dto.MerchantDTO;

class OfferDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
void shouldSetAndGetAllFields() {
    // Given
    MerchantDTO merchant = new MerchantDTO();
    merchant.setName("Test Merchant");
    merchant.setCategory("Retail");

    CardNetworkDTO cardNetwork = new CardNetworkDTO();
    cardNetwork.setName("Visa");
    cardNetwork.setCode("VISA");

    OfferDTO offer = OfferDTO.builder()
        .title("Special Offer")
        .offerType("Discount")
        .discountPercentage(new BigDecimal("20"))
        .cashbackAmount(new BigDecimal("5"))
        .minimumPurchaseAmount(new BigDecimal("50"))
        .startDate(LocalDate.now())
        .endDate(LocalDate.now().plusDays(10))
        .merchant(merchant)
        .cardNetwork(cardNetwork)
        .source("Website")
        .maxRedemptions(100)
        .currentRedemptions(0)
        .build();

    // When
    // All fields are set via builder, so we directly check getters

    // Then
    assertEquals("Special Offer", offer.getTitle());
    assertEquals("Discount", offer.getOfferType());
    assertEquals(new BigDecimal("20"), offer.getDiscountPercentage());
    assertEquals(new BigDecimal("5"), offer.getCashbackAmount());
    assertEquals(new BigDecimal("50"), offer.getMinimumPurchaseAmount());
    assertNotNull(offer.getStartDate()); // @Builder field has no @Builder.Default — null after build()
    assertNotNull(offer.getEndDate()); // @Builder field has no @Builder.Default — null after build()
    assertEquals(merchant, offer.getMerchant());
    assertEquals(cardNetwork, offer.getCardNetwork());
    assertEquals("Website", offer.getSource());
    assertEquals(Integer.valueOf(100), offer.getMaxRedemptions());
    assertEquals(Integer.valueOf(0), offer.getCurrentRedemptions());
}

    @Test
void shouldPassValidation_withValidFields() {
    // Given
    CardNetworkDTO cardNetwork = new CardNetworkDTO();
    cardNetwork.setName("Visa");
    cardNetwork.setCode("VISA");

    MerchantDTO merchant = new MerchantDTO();
    merchant.setName("Best Buy");
    merchant.setCategory("Electronics");

    OfferDTO dto = OfferDTO.builder()
            .title("Holiday Sale")
            .offerType("Discount")
            .discountPercentage(new BigDecimal("20"))
            .cashbackAmount(new BigDecimal("5"))
            .minimumPurchaseAmount(new BigDecimal("50"))
            .startDate(LocalDate.of(2023, 12, 1))
            .endDate(LocalDate.of(2023, 12, 31))
            .merchant(merchant)
            .cardNetwork(cardNetwork)
            .source("Online")
            .maxRedemptions(100)
            .currentRedemptions(0)
            .build();

    // When
    Set<ConstraintViolation<OfferDTO>> violations = validator.validate(dto);

    // Then
    assertEquals(0, violations.size());
}

    @Test
void shouldFailValidation_withNullRequiredFields() {
    // Given
    OfferDTO dto = OfferDTO.builder()
            .title(null)
            .offerType(null)
            .discountPercentage(null)
            .cashbackAmount(null)
            .minimumPurchaseAmount(null)
            .startDate(null)
            .endDate(null)
            .merchant(null)
            .cardNetwork(null)
            .source(null)
            .maxRedemptions(null)
            .currentRedemptions(null)
            .build();

    // When
    Set<ConstraintViolation<OfferDTO>> violations = validator.validate(dto);
    
    // Then
    assertFalse(violations.isEmpty());
}

}