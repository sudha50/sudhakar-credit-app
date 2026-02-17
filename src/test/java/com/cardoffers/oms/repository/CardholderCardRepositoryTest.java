package com.cardoffers.oms.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import com.cardoffers.oms.model.entity.CardholderCard;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CardholderCardRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    CardholderCardRepository cardholderCardRepository;

    @Test
    void shouldReturnCards_whenCardholderIdExists() {
        Long cardholderId = 1L;
        CardholderCard card = new CardholderCard();
        card.setCardholderId(cardholderId);
        card.setActive(true);
        entityManager.persist(card);
        
        List<CardholderCard> cards = cardholderCardRepository.findByCardholderId(cardholderId);
        
        assertNotNull(cards);
        assertEquals(1, cards.size());
        assertEquals(cardholderId, cards.get(0).getCardholderId());
    }

    @Test
    void shouldReturnActiveCards_whenCardholderIdExists() {
        Long cardholderId = 2L;
        CardholderCard activeCard = new CardholderCard();
        activeCard.setCardholderId(cardholderId);
        activeCard.setActive(true);
        entityManager.persist(activeCard);

        CardholderCard inactiveCard = new CardholderCard();
        inactiveCard.setCardholderId(cardholderId);
        inactiveCard.setActive(false);
        entityManager.persist(inactiveCard);
        
        List<CardholderCard> activeCards = cardholderCardRepository.findByCardholderIdAndActiveTrue(cardholderId);
        
        assertNotNull(activeCards);
        assertEquals(1, activeCards.size());
        assertTrue(activeCards.get(0).isActive());
    }

    @Test
    void shouldReturnEmptyList_whenNoCardsForCardholderId() {
        Long cardholderId = 3L;
        
        List<CardholderCard> cards = cardholderCardRepository.findByCardholderId(cardholderId);
        
        assertNotNull(cards);
        assertTrue(cards.isEmpty());
    }

    @Test
    void shouldReturnEmptyList_whenNoActiveCardsForCardholderId() {
        Long cardholderId = 4L;
        CardholderCard card = new CardholderCard();
        card.setCardholderId(cardholderId);
        card.setActive(false);
        entityManager.persist(card);
        
        List<CardholderCard> activeCards = cardholderCardRepository.findByCardholderIdAndActiveTrue(cardholderId);
        
        assertNotNull(activeCards);
        assertTrue(activeCards.isEmpty());
    }
}