package com.cardoffers.oms.model.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OfferTest {

    private Offer offer;

    @BeforeEach
    void setUp() {
        offer = new Offer();
    }

    @Test
    void shouldInitializeCurrentRedemptionsToZero_whenPrePersistIsCalledAndCurrentRedemptionsIsNull() {
        offer.setCurrentRedemptions(null);
        offer.prePersist();
        assertEquals(0, offer.getCurrentRedemptions());
    }

    @Test
    void shouldInitializeActiveToTrue_whenPrePersistIsCalledAndActiveIsNull() {
        offer.setActive(null);
        offer.prePersist();
        assertTrue(offer.getActive());
    }

    @Test
    void shouldSetCreatedAtAndUpdatedAtToNow_whenPrePersistIsCalled() {
        offer.prePersist();
        LocalDateTime now = LocalDateTime.now();
        assertTrue(offer.getCreatedAt().isBefore(now.plusSeconds(1)) && offer.getCreatedAt().isAfter(now.minusSeconds(1)));
        assertTrue(offer.getUpdatedAt().isBefore(now.plusSeconds(1)) && offer.getUpdatedAt().isAfter(now.minusSeconds(1)));
    }

    @Test
    void shouldUpdateUpdatedAtToNow_whenPreUpdateIsCalled() {
        offer.setUpdatedAt(LocalDateTime.of(2022, 1, 1, 0, 0));
        offer.preUpdate();
        LocalDateTime now = LocalDateTime.now();
        assertTrue(offer.getUpdatedAt().isBefore(now.plusSeconds(1)) && offer.getUpdatedAt().isAfter(now.minusSeconds(1)));
    }
}