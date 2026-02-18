package com.cardoffers.oms.model.dto;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class OfferDTOFunctionalTest {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldPassValidationWithAllValidFields() {
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setId(1L);
        offerDTO.setTitle("New Year Offer");
        offerDTO.setDescription("A special offer for New Year");
        offerDTO.setOfferType("Discount");
        offerDTO.setDiscountPercentage(BigDecimal.valueOf(20));
        offerDTO.setCashbackAmount(BigDecimal.valueOf(5));
        offerDTO.setMinimumPurchaseAmount(BigDecimal.valueOf(50));
        offerDTO.setStartDate(LocalDate.now());
        offerDTO.setEndDate(LocalDate.now().plusDays(30));
        offerDTO.setTermsAndConditions("Terms apply");
        
        MerchantDTO merchantDTO = new MerchantDTO();
        merchantDTO.setId(1L);
        offerDTO.setMerchant(merchantDTO);
        
        CardNetworkDTO cardNetworkDTO = new CardNetworkDTO();
        cardNetworkDTO.setId(1L);
        offerDTO.setCardNetwork(cardNetworkDTO);
        
        offerDTO.setSource("Online");
        offerDTO.setMaxRedemptions(100);
        offerDTO.setCurrentRedemptions(0);
        offerDTO.setActive(true);

        var violations = validator.validate(offerDTO);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailValidationWhenTitleIsBlank() {
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setTitle("");
        
        var violations = validator.validate(offerDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailValidationWhenStartDateIsNull() {
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setStartDate(null);
        
        var violations = validator.validate(offerDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailValidationWhenMerchantIsNull() {
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setMerchant(null);
        
        var violations = validator.validate(offerDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailValidationWhenDiscountPercentageIsNegative() {
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setDiscountPercentage(BigDecimal.valueOf(-10));

        var violations = validator.validate(offerDTO);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailValidationWhenCurrentRedemptionsIsNegative() {
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setCurrentRedemptions(-1);

        var violations = validator.validate(offerDTO);
        assertFalse(violations.isEmpty());
    }
}