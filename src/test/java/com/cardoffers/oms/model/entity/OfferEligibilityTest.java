package com.cardoffers.oms.model.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class OfferEligibilityTest {

    private OfferEligibility offerEligibility;

    @BeforeEach
    void setUp() {
        offerEligibility = new OfferEligibility();
    }

    @Test
    void shouldSetCheckedAt_whenPrePersistCalledAndCheckedAtIsNull() {
        offerEligibility.setCheckedAt(null);
        offerEligibility.prePersist();
        assertNotNull(offerEligibility.getCheckedAt());
    }

    @Test
    void shouldNotChangeCheckedAt_whenPrePersistCalledAndCheckedAtIsNotNull() {
        LocalDateTime existingTime = LocalDateTime.now();
        offerEligibility.setCheckedAt(existingTime);
        offerEligibility.prePersist();
        assertEquals(existingTime, offerEligibility.getCheckedAt());
    }

    @Test
    void shouldAllowValidOffer_whenOfferIsSet() {
        Offer offer = new Offer();
        offerEligibility.setOffer(offer);
        assertNotNull(offerEligibility.getOffer());
    }

    @Test
    void shouldAllowValidCardholder_whenCardholderIsSet() {
        Cardholder cardholder = new Cardholder();
        offerEligibility.setCardholder(cardholder);
        assertNotNull(offerEligibility.getCardholder());
    }

    @Test
    void shouldAllowIsEligibleField_whenSet() {
        offerEligibility.setIsEligible(true);
        assertTrue(offerEligibility.getIsEligible());
    }

    @Test
    void shouldAllowEligibilityReasonField_whenSet() {
        String reason = "Applicant meets all criteria.";
        offerEligibility.setEligibilityReason(reason);
        assertEquals(reason, offerEligibility.getEligibilityReason());
    }
}