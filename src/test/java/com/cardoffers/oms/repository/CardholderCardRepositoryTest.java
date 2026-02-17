package com.cardoffers.oms.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import com.cardoffers.oms.model.entity.CardholderCard;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
class CardholderCardRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    CardholderCardRepository cardholderCardRepository;

    @Test
    void shouldReturnEmptyList_whenNoCardholderCardsExist() {
        List<CardholderCard> result = cardholderCardRepository.findByCardholderId(1L);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnCardholderCards_whenCardsExistForCardholder() {
        CardholderCard card = new CardholderCard();
        card.setCardholderId(1L);
        card.setActive(true);
        entityManager.persist(card);
        entityManager.flush();

        List<CardholderCard> result = cardholderCardRepository.findByCardholderId(1L);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getCardholderId());
    }

    @Test
    void shouldReturnActiveCardholderCards_whenActiveCardsExistForCardholder() {
        CardholderCard activeCard = new CardholderCard();
        activeCard.setCardholderId(1L);
        activeCard.setActive(true);
        entityManager.persist(activeCard);
        
        CardholderCard inactiveCard = new CardholderCard();
        inactiveCard.setCardholderId(1L);
        inactiveCard.setActive(false);
        entityManager.persist(inactiveCard);
        
        entityManager.flush();

        List<CardholderCard> result = cardholderCardRepository.findByCardholderIdAndActiveTrue(1L);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertTrue(result.get(0).isActive());
    }

    @Test
    void shouldReturnEmptyList_whenNoActiveCardsExistForCardholder() {
        CardholderCard inactiveCard = new CardholderCard();
        inactiveCard.setCardholderId(2L);
        inactiveCard.setActive(false);
        entityManager.persist(inactiveCard);
        entityManager.flush();

        List<CardholderCard> result = cardholderCardRepository.findByCardholderIdAndActiveTrue(2L);
        assertTrue(result.isEmpty());
    }
}