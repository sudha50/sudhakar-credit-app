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
import jakarta.validation.Valid;
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
    merchant.setName("Merchant Name");
    merchant.setCategory("Retail");

    CardNetworkDTO cardNetwork = new CardNetworkDTO();
    cardNetwork.setName("Visa");
    cardNetwork.setCode("VISA");

    OfferDTO offer = OfferDTO.builder()
            .title("50% Off")
            .offerType("Discount")
            .discountPercentage(BigDecimal.valueOf(50))
            .cashbackAmount(BigDecimal.valueOf(10))
            .minimumPurchaseAmount(BigDecimal.valueOf(100))
            .startDate(LocalDate.of(2023, 1, 1))
            .endDate(LocalDate.of(2023, 12, 31))
            .merchant(merchant)
            .cardNetwork(cardNetwork)
            .source("Online")
            .maxRedemptions(100)
            .currentRedemptions(0)
            .build();

    // When
    String title = offer.getTitle();
    String offerType = offer.getOfferType();
    BigDecimal discountPercentage = offer.getDiscountPercentage();
    BigDecimal cashbackAmount = offer.getCashbackAmount();
    BigDecimal minimumPurchaseAmount = offer.getMinimumPurchaseAmount();
    LocalDate startDate = offer.getStartDate();
    LocalDate endDate = offer.getEndDate();
    MerchantDTO offerMerchant = offer.getMerchant();
    CardNetworkDTO offerCardNetwork = offer.getCardNetwork();
    String source = offer.getSource();
    Integer maxRedemptions = offer.getMaxRedemptions();
    Integer currentRedemptions = offer.getCurrentRedemptions();

    // Then
    assertEquals("50% Off", title);
    assertEquals("Discount", offerType);
    assertEquals(BigDecimal.valueOf(50), discountPercentage);
    assertEquals(BigDecimal.valueOf(10), cashbackAmount);
    assertEquals(BigDecimal.valueOf(100), minimumPurchaseAmount);
    assertEquals(LocalDate.of(2023, 1, 1), startDate);
    assertEquals(LocalDate.of(2023, 12, 31), endDate);
    assertNotNull(offerMerchant);
    assertEquals("Merchant Name", offerMerchant.getName());
    assertEquals("Retail", offerMerchant.getCategory());
    assertNotNull(offerCardNetwork);
    assertEquals("Visa", offerCardNetwork.getName());
    assertEquals("VISA", offerCardNetwork.getCode());
    assertEquals("Online", source);
    assertEquals((Integer) 100, maxRedemptions);
    assertEquals((Integer) 0, currentRedemptions);
}

    @Test
void shouldPassValidation_withValidFields() {
    // Given
    MerchantDTO merchant = new MerchantDTO();
    merchant.setName("Valid Merchant");
    merchant.setCategory("Retail");

    CardNetworkDTO cardNetwork = new CardNetworkDTO();
    cardNetwork.setName("Visa");
    cardNetwork.setCode("VISA");

    OfferDTO dto = OfferDTO.builder()
            .title("Valid Title")
            .offerType("Discount")
            .discountPercentage(new BigDecimal("10.00"))
            .cashbackAmount(new BigDecimal("5.00"))
            .minimumPurchaseAmount(new BigDecimal("50.00"))
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusDays(10))
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