package com.cardoffers.oms.model.entity;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

public class CardNetworkTest {

    @Test
    void shouldSetCreatedAt_whenPrePersistIsCalled() {
        CardNetwork cardNetwork = new CardNetwork();
        cardNetwork.prePersist();
        assertNotNull(cardNetwork.getCreatedAt());
    }

    @Test
    void shouldSetActiveToTrue_whenActiveIsNullAndPrePersistIsCalled() {
        CardNetwork cardNetwork = new CardNetwork();
        cardNetwork.setActive(null);
        cardNetwork.prePersist();
        assertTrue(cardNetwork.getActive());
    }

    @Test
    void shouldRetainActiveValue_whenActiveIsNotNullAndPrePersistIsCalled() {
        CardNetwork cardNetwork = new CardNetwork();
        cardNetwork.setActive(false);
        cardNetwork.prePersist();
        assertFalse(cardNetwork.getActive());
    }

    @Test
    void shouldHaveDefaultOffersSet_whenCardNetworkIsCreated() {
        CardNetwork cardNetwork = new CardNetwork();
        assertNotNull(cardNetwork.getOffers());
        assertTrue(cardNetwork.getOffers().isEmpty());
    }

    @Test
    void shouldHaveNullIdAtCreationTime() {
        CardNetwork cardNetwork = new CardNetwork();
        assertNull(cardNetwork.getId());
    }
}