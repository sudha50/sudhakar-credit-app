package com.cardoffers.oms.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import com.cardoffers.oms.model.entity.CardholderCard;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CardholderCardRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    CardholderCardRepository cardholderCardRepository;

    @Test
    void shouldReturnCards_whenCardholderIdIsGiven() {
        CardholderCard card = new CardholderCard();
        card.setCardholderId(1L);
        card.setActive(true);
        entityManager.persist(card);
        
        List<CardholderCard> cards = cardholderCardRepository.findByCardholderId(1L);
        
        assertNotNull(cards);
        assertEquals(1, cards.size());
        assertEquals(1L, cards.get(0).getCardholderId());
    }

    @Test
    void shouldReturnActiveCards_whenCardholderIdIsGiven() {
        CardholderCard card1 = new CardholderCard();
        card1.setCardholderId(1L);
        card1.setActive(true);
        entityManager.persist(card1);
        
        CardholderCard card2 = new CardholderCard();
        card2.setCardholderId(1L);
        card2.setActive(false);
        entityManager.persist(card2);
        
        List<CardholderCard> activeCards = cardholderCardRepository.findByCardholderIdAndActiveTrue(1L);
        
        assertNotNull(activeCards);
        assertEquals(1, activeCards.size());
        assertTrue(activeCards.get(0).isActive());
    }

    @Test
    void shouldReturnEmptyList_whenNoCardsFoundForCardholderId() {
        List<CardholderCard> cards = cardholderCardRepository.findByCardholderId(2L);
        
        assertNotNull(cards);
        assertTrue(cards.isEmpty());
    }

    @Test
    void shouldReturnEmptyList_whenNoActiveCardsFoundForCardholderId() {
        CardholderCard card = new CardholderCard();
        card.setCardholderId(1L);
        card.setActive(false);
        entityManager.persist(card);
        
        List<CardholderCard> activeCards = cardholderCardRepository.findByCardholderIdAndActiveTrue(1L);
        
        assertNotNull(activeCards);
        assertTrue(activeCards.isEmpty());
    }
}