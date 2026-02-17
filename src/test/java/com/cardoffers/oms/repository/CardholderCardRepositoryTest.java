package com.cardoffers.oms.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import com.cardoffers.oms.model.entity.CardholderCard;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
class CardholderCardRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    CardholderCardRepository cardholderCardRepository;

    @Test
    void shouldReturnCards_whenCardholderIdIsValid() {
        // Arrange
        CardholderCard card = new CardholderCard();
        card.setCardholderId(1L);
        card.setActive(true);
        entityManager.persist(card);
        
        // Act
        List<CardholderCard> result = cardholderCardRepository.findByCardholderId(1L);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(card.getCardholderId(), result.get(0).getCardholderId());
    }

    @Test
    void shouldReturnActiveCards_whenCardholderIdIsValid() {
        // Arrange
        CardholderCard activeCard = new CardholderCard();
        activeCard.setCardholderId(2L);
        activeCard.setActive(true);
        entityManager.persist(activeCard);

        CardholderCard inactiveCard = new CardholderCard();
        inactiveCard.setCardholderId(2L);
        inactiveCard.setActive(false);
        entityManager.persist(inactiveCard);
        
        // Act
        List<CardholderCard> result = cardholderCardRepository.findByCardholderIdAndActiveTrue(2L);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).isActive());
    }

    @Test
    void shouldReturnEmptyList_whenNoCardsForCardholderId() {
        // Act
        List<CardholderCard> result = cardholderCardRepository.findByCardholderId(999L);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyList_whenNoActiveCardsForCardholderId() {
        // Arrange
        CardholderCard card = new CardholderCard();
        card.setCardholderId(3L);
        card.setActive(false);
        entityManager.persist(card);
        
        // Act
        List<CardholderCard> result = cardholderCardRepository.findByCardholderIdAndActiveTrue(3L);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}