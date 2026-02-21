package com.cardoffers.oms.model.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class OfferTest {

    private Offer offer;

    @BeforeEach
    void setUp() {
        offer = new Offer();
        offer.setTitle("Summer Sale");
        offer.setDescription("Enjoy discounts this summer.");
        offer.setOfferType("percentage");
        offer.setDiscountPercentage(new BigDecimal("20.00"));
        offer.setCashbackAmount(new BigDecimal("5.00"));
        offer.setMinimumPurchaseAmount(new BigDecimal("50.00"));
        offer.setStartDate(LocalDate.now());
        offer.setEndDate(LocalDate.now().plusDays(30));
        offer.setTermsAndConditions("Terms apply.");
        offer.setMerchant(new Merchant());
        offer.setCardNetwork(new CardNetwork());
        offer.setSource("Online");
        offer.setMaxRedemptions(100);
        offer.setCurrentRedemptions(null);
        offer.setActive(null);
    }

    @Test
    void shouldInitializeCurrentRedemptionsAndActive_whenPrePersistIsCalled() {
        offer.prePersist();
        assertEquals(0, offer.getCurrentRedemptions());
        assertTrue(offer.getActive());
    }

    @Test
    void shouldNotModifyCurrentRedemptions_whenPrePersistIsCalledWithValue() {
        offer.setCurrentRedemptions(10);
        offer.prePersist();
        assertEquals(10, offer.getCurrentRedemptions());
    }

    @Test
    void shouldNotModifyActive_whenPrePersistIsCalledWithValue() {
        offer.setActive(false);
        offer.prePersist();
        assertFalse(offer.getActive());
    }

    @Test
    void shouldUpdateUpdatedAt_whenPreUpdateIsCalled() {
        LocalDateTime beforeUpdate = LocalDateTime.now();
        offer.preUpdate();
        LocalDateTime afterUpdate = offer.getUpdatedAt();
        assertTrue(afterUpdate.isAfter(beforeUpdate));
    }

    @Test
    void shouldSetCreatedAtAndUpdatedAt_whenOfferIsPersisted() {
        offer.prePersist();
        assertNotNull(offer.getCreatedAt());
        assertNotNull(offer.getUpdatedAt());
    }

    @Test
    void shouldUpdateUpdatedAtValue_whenOfferIsUpdated() {
        offer.setCurrentRedemptions(5);
        offer.preUpdate();
        assertNotNull(offer.getUpdatedAt());
    }
}