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
    void shouldSetCheckedAtToNow_whenPrePersistIsCalledAndCheckedAtIsNull() {
        offerEligibility.prePersist();
        assertNotNull(offerEligibility.getCheckedAt());
    }

    @Test
    void shouldNotChangeCheckedAt_whenPrePersistIsCalledAndCheckedAtIsAlreadySet() {
        LocalDateTime now = LocalDateTime.now();
        offerEligibility.setCheckedAt(now);
        offerEligibility.prePersist();
        assertEquals(now, offerEligibility.getCheckedAt());
    }

    @Test
    void shouldInstantiateOfferEligibilityWithDefaults_whenNoArgsConstructorCalled() {
        OfferEligibility eligibility = new OfferEligibility();
        assertNull(eligibility.getId());
        assertNull(eligibility.getOffer());
        assertNull(eligibility.getCardholder());
        assertNull(eligibility.getIsEligible());
        assertNull(eligibility.getEligibilityReason());
        assertNull(eligibility.getCheckedAt());
    }

    @Test
    void shouldSetValuesCorrectly_whenAllArgsConstructorUsed() {
        Offer offer = new Offer();
        Cardholder cardholder = new Cardholder();
        offerEligibility = new OfferEligibility(1L, offer, cardholder, true, "Eligible", LocalDateTime.now());
        
        assertEquals(1L, offerEligibility.getId());
        assertEquals(offer, offerEligibility.getOffer());
        assertEquals(cardholder, offerEligibility.getCardholder());
        assertTrue(offerEligibility.getIsEligible());
        assertEquals("Eligible", offerEligibility.getEligibilityReason());
        assertNotNull(offerEligibility.getCheckedAt());
    }
}