package com.cardoffers.oms.model.dto;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class OfferDTOTest {

    @Test
    void shouldCreateOfferDTOWithAllFields_whenValidParameters() {
        MerchantDTO merchant = new MerchantDTO();
        merchant.setId(1L);
        CardNetworkDTO cardNetwork = new CardNetworkDTO();
        cardNetwork.setId(1L);

        OfferDTO offer = new OfferDTO();
        offer.setId(1L);
        offer.setTitle("Special Offer");
        offer.setDescription("Amazing discount!");
        offer.setOfferType("Discount");
        offer.setDiscountPercentage(BigDecimal.valueOf(20));
        offer.setCashbackAmount(BigDecimal.valueOf(5));
        offer.setMinimumPurchaseAmount(BigDecimal.valueOf(50));
        offer.setStartDate(LocalDate.now());
        offer.setEndDate(LocalDate.now().plusDays(30));
        offer.setMerchant(merchant);
        offer.setCardNetwork(cardNetwork);
        offer.setSource("Website");
        offer.setMaxRedemptions(100);
        offer.setCurrentRedemptions(0);
        offer.setActive(true);

        assertNotNull(offer);
        assertEquals("Special Offer", offer.getTitle());
        assertEquals(BigDecimal.valueOf(20), offer.getDiscountPercentage());
        assertTrue(offer.getActive());
    }

    @Test
    void shouldThrowValidationException_whenTitleIsBlank() {
        OfferDTO offer = new OfferDTO();
        offer.setTitle("");
        offer.setOfferType("Discount");
        offer.setStartDate(LocalDate.now());
        offer.setEndDate(LocalDate.now().plusDays(30));
        assertThrows(javax.validation.ConstraintViolationException.class, () -> {
            // Perform validation (simulated, as actual validation needs a Validator instance)
            validate(offer);
        });
    }

    @Test
    void shouldThrowValidationException_whenOfferTypeIsBlank() {
        OfferDTO offer = new OfferDTO();
        offer.setTitle("Special Offer");
        offer.setOfferType("");
        offer.setStartDate(LocalDate.now());
        offer.setEndDate(LocalDate.now().plusDays(30));
        assertThrows(javax.validation.ConstraintViolationException.class, () -> {
            validate(offer);
        });
    }

    @Test
    void shouldThrowValidationException_whenMerchantIsNull() {
        OfferDTO offer = new OfferDTO();
        offer.setTitle("Special Offer");
        offer.setOfferType("Discount");
        offer.setStartDate(LocalDate.now());
        offer.setEndDate(LocalDate.now().plusDays(30));
        offer.setMerchant(null);
        assertThrows(javax.validation.ConstraintViolationException.class, () -> {
            validate(offer);
        });
    }

    @Test
    void shouldThrowValidationException_whenStartDateIsNull() {
        MerchantDTO merchant = new MerchantDTO();
        merchant.setId(1L);
        OfferDTO offer = new OfferDTO();
        offer.setTitle("Special Offer");
        offer.setOfferType("Discount");
        offer.setStartDate(null);
        offer.setEndDate(LocalDate.now().plusDays(30));
        offer.setMerchant(merchant);
        assertThrows(javax.validation.ConstraintViolationException.class, () -> {
            validate(offer);
        });
    }

    @Test
    void shouldThrowValidationException_whenEndDateIsNull() {
        MerchantDTO merchant = new MerchantDTO();
        merchant.setId(1L);
        OfferDTO offer = new OfferDTO();
        offer.setTitle("Special Offer");
        offer.setOfferType("Discount");
        offer.setStartDate(LocalDate.now());
        offer.setEndDate(null);
        offer.setMerchant(merchant);
        assertThrows(javax.validation.ConstraintViolationException.class, () -> {
            validate(offer);
        });
    }

    private void validate(OfferDTO offer) {
        // Placeholder for validation logic, should use a Validator instance
        // to check the states, throwing ConstraintViolationException if invalid
    }
}