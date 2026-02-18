package com.cardoffers.oms.model.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        cardholder = new Cardholder(); // Assuming a no-arg constructor is present
        cardholder.setId(1L);
        
        cardNetwork = new CardNetwork(); // Assuming a no-arg constructor is present
        cardNetwork.setId(1L);
        
        cardholderCard = new CardholderCard();
        cardholderCard.setCardholder(cardholder);
        cardholderCard.setCardNetwork(cardNetwork);
        cardholderCard.setCardNumberLastFour("1234");
        cardholderCard.setCardType("Visa");
    }

    @Test
    void shouldSetCreatedAt_whenPrePersistIsCalled() {
        cardholderCard.prePersist();
        assertNotNull(cardholderCard.getCreatedAt(), "CreatedAt should not be null after prePersist is called");
    }

    @Test
    void shouldSetActiveToTrue_whenActiveIsNullBeforePersist() {
        cardholderCard.setActive(null);
        cardholderCard.prePersist();
        assertTrue(cardholderCard.getActive(), "Active should be set to true when it is null before persist");
    }

    @Test
    void shouldNotChangeActive_whenActiveIsNotNullBeforePersist() {
        cardholderCard.setActive(false);
        cardholderCard.prePersist();
        assertFalse(cardholderCard.getActive(), "Active should remain false when it is already set before persist");
    }

    @Test
    void shouldReturnCorrectCardNumberLastFour_whenCardNumberLastFourIsSet() {
        assertEquals("1234", cardholderCard.getCardNumberLastFour(), "Card number last four should match the set value");
    }
}