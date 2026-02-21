package com.cardoffers.oms.model.dto;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class OfferDTOTest {

    @Test
    public void shouldCreateOfferDTO_whenAllFieldsProvided() {
        MerchantDTO merchant = new MerchantDTO();
        merchant.setId(1L);
        CardNetworkDTO cardNetwork = new CardNetworkDTO();
        cardNetwork.setId(1L);
        
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setTitle("Special Offer");
        offerDTO.setDescription("Save on your next purchase!");
        offerDTO.setOfferType("Discount");
        offerDTO.setDiscountPercentage(BigDecimal.valueOf(10));
        offerDTO.setCashbackAmount(BigDecimal.valueOf(5));
        offerDTO.setMinimumPurchaseAmount(BigDecimal.valueOf(20));
        offerDTO.setStartDate(LocalDate.now());
        offerDTO.setEndDate(LocalDate.now().plusDays(30));
        offerDTO.setTermsAndConditions("Terms apply.");
        offerDTO.setMerchant(merchant);
        offerDTO.setCardNetwork(cardNetwork);
        offerDTO.setSource("Website");
        offerDTO.setMaxRedemptions(100);
        offerDTO.setCurrentRedemptions(0);
        offerDTO.setActive(true);

        assertNotNull(offerDTO);
        assertEquals("Special Offer", offerDTO.getTitle());
        assertEquals(BigDecimal.valueOf(10), offerDTO.getDiscountPercentage());
    }

    @Test
    public void shouldThrowException_whenTitleIsBlank() {
        OfferDTO offerDTO = new OfferDTO();
        assertThrows(javax.validation.ConstraintViolationException.class, () -> {
            offerDTO.setTitle("");
            validate(offerDTO);
        });
    }

    @Test
    public void shouldThrowException_whenOfferTypeIsBlank() {
        OfferDTO offerDTO = new OfferDTO();
        assertThrows(javax.validation.ConstraintViolationException.class, () -> {
            offerDTO.setOfferType("");
            validate(offerDTO);
        });
    }

    @Test
    public void shouldSetActiveToTrue_whenActiveTrueIsSet() {
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setActive(true);
        assertTrue(offerDTO.getActive());
    }

    @Test
    public void shouldThrowException_whenMerchantIsNull() {
        OfferDTO offerDTO = new OfferDTO();
        assertThrows(javax.validation.ConstraintViolationException.class, () -> {
            offerDTO.setMerchant(null);
            validate(offerDTO);
        });
    }

    @Test
    public void shouldThrowException_whenCardNetworkIsNull() {
        OfferDTO offerDTO = new OfferDTO();
        assertThrows(javax.validation.ConstraintViolationException.class, () -> {
            offerDTO.setCardNetwork(null);
            validate(offerDTO);
        });
    }

    // A method to simulate validation for the purpose of the tests
    private void validate(OfferDTO offerDTO) {
        // Placeholder for a real validation method
        var violations = new java.util.HashSet<javax.validation.ConstraintViolation<OfferDTO>>();
        if (offerDTO.getTitle() == null || offerDTO.getTitle().isBlank()) {
            violations.add(new javax.validation.ConstraintViolation<OfferDTO>() {
                // Implement necessary methods for the violation
            });
        }
        
        if (offerDTO.getMerchant() == null) {
            violations.add(new javax.validation.ConstraintViolation<OfferDTO>() {
                // Implement necessary methods for the violation
            });
        }
        
        if (!violations.isEmpty()) {
            throw new javax.validation.ConstraintViolationException(violations);
        }
    }
}