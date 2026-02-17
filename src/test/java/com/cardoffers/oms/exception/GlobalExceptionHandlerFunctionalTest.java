package com.cardoffers.oms.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import com.cardoffers.oms.model.entity.CardholderCard;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CardholderCardRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CardholderCardRepository cardholderCardRepository;

    @Test
    void shouldReturnCards_whenCardholderIdIsGiven() {
        CardholderCard card = new CardholderCard();
        card.setCardholderId(1L);     // Ensure this method exists in your CardholderCard class
        card.setActive(true);          // Ensure this method exists in your CardholderCard class
        entityManager.persist(card);
        entityManager.flush();

        List<CardholderCard> cards = cardholderCardRepository.findByCardholderId(1L);

        assertNotNull(cards);
        assertEquals(1, cards.size());
        assertNotNull(cards.get(0)); // Additional null check
        assertEquals(1L, cards.get(0).getCardholderId()); // Check if cardholderId is retrieved correctly
    }

    @Test
    void shouldReturnActiveCards_whenCardholderIdIsGiven() {
        CardholderCard card1 = new CardholderCard();
        card1.setCardholderId(1L);  // Ensure this method exists
        card1.setActive(true);       // Ensure this method exists
        entityManager.persist(card1);
        
        CardholderCard card2 = new CardholderCard();
        card2.setCardholderId(1L);  // Ensure this method exists
        card2.setActive(false);      // Ensure this method exists
        entityManager.persist(card2);
        
        entityManager.flush();

        List<CardholderCard> activeCards = cardholderCardRepository.findByCardholderIdAndActiveTrue(1L);
        
        assertNotNull(activeCards);
        assertEquals(1, activeCards.size());
        assertTrue(activeCards.get(0).isActive()); // Check if card is active
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
        card.setCardholderId(1L); // Ensure proper method exists
        card.setActive(false);     // Ensure proper method exists
        entityManager.persist(card);
        
        entityManager.flush();

        List<CardholderCard> activeCards = cardholderCardRepository.findByCardholderIdAndActiveTrue(1L);
        
        assertNotNull(activeCards);
        assertTrue(activeCards.isEmpty());
    }
}
