package com.cardoffers.oms.model.entity;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardholderCardTest {

    private CardholderCard cardholderCard;
    private Cardholder cardholder;
    private CardNetwork cardNetwork;

    @BeforeEach
    void setUp() {
        cardholder = new Cardholder();
        cardholder.setId(1L); // Assuming Cardholder has a setId method
        cardNetwork = new CardNetwork();
        cardNetwork.setId(1L); // Assuming CardNetwork has a setId method
        cardholderCard = new CardholderCard();
        cardholderCard.setCardholder(cardholder);
        cardholderCard.setCardNetwork(cardNetwork);
        cardholderCard.setCardNumberLastFour("1234");
        cardholderCard.setCardType("Visa");
    }

    @Test
    void shouldSetCreatedAtAndActive_whenPrePersistIsCalled() {
        cardholderCard.prePersist();
        
        assertNotNull(cardholderCard.getCreatedAt());
        assertTrue(cardholderCard.getActive());
    }

    @Test
    void shouldDefaultActiveToTrue_whenActiveIsNullInPrePersist() {
        cardholderCard.setActive(null);
        cardholderCard.prePersist();
        
        assertTrue(cardholderCard.getActive());
    }

    @Test
    void shouldPreserveActiveValue_whenActiveIsSet() {
        cardholderCard.setActive(false);
        cardholderCard.prePersist();

        assertFalse(cardholderCard.getActive());
    }

    @Test
    void shouldSetCreatedAtToCurrentTime_whenPrePersistIsCalled() {
        cardholderCard.prePersist();
        
        LocalDateTime now = LocalDateTime.now();
        assertTrue(cardholderCard.getCreatedAt().isBefore(now.plusSeconds(1)));
        assertTrue(cardholderCard.getCreatedAt().isAfter(now.minusSeconds(1)));
    }
}