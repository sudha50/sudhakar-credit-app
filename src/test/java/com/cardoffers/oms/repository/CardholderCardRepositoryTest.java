package com.cardoffers.oms.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import com.cardoffers.oms.model.entity.CardholderCard;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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
    void shouldReturnEmptyList_whenCardholderIdNotExists() {
        List<CardholderCard> result = cardholderCardRepository.findByCardholderId(999L);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnListOfCards_whenCardholderIdExists() {
        CardholderCard card = new CardholderCard();
        card.setCardholderId(1L);
        card.setActive(true);
        entityManager.persist(card);
        entityManager.flush();
        
        List<CardholderCard> result = cardholderCardRepository.findByCardholderId(1L);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getCardholderId());
    }

    @Test
    void shouldReturnEmptyList_whenCardholderIdExistsButNoActiveCards() {
        CardholderCard card = new CardholderCard();
        card.setCardholderId(1L);
        card.setActive(false);
        entityManager.persist(card);
        entityManager.flush();

        List<CardholderCard> result = cardholderCardRepository.findByCardholderIdAndActiveTrue(1L);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnActiveCards_whenCardholderIdExistsWithActiveCards() {
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
        assertEquals(1, result.size());
        assertTrue(result.get(0).isActive());
    }
}