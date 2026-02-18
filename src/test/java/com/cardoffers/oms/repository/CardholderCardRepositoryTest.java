package com.cardoffers.oms.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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
    void shouldReturnEmptyList_whenCardholderIdNotExists() {
        List<CardholderCard> cards = cardholderCardRepository.findByCardholderId(999L);
        assertTrue(cards.isEmpty());
    }

    @Test
    void shouldReturnCards_whenCardholderIdExists() {
        CardholderCard card = new CardholderCard();
        card.setCardholderId(1L);
        card.setActive(true);
        entityManager.persist(card);
        entityManager.flush();

        List<CardholderCard> cards = cardholderCardRepository.findByCardholderId(1L);
        assertEquals(1, cards.size());
        assertEquals(card.getCardholderId(), cards.get(0).getCardholderId());
    }

    @Test
    void shouldReturnActiveCardsOnly_whenQueryingActiveTrue() {
        CardholderCard activeCard = new CardholderCard();
        activeCard.setCardholderId(1L);
        activeCard.setActive(true);
        entityManager.persist(activeCard);

        CardholderCard inactiveCard = new CardholderCard();
        inactiveCard.setCardholderId(1L);
        inactiveCard.setActive(false);
        entityManager.persist(inactiveCard);

        entityManager.flush();

        List<CardholderCard> activeCards = cardholderCardRepository.findByCardholderIdAndActiveTrue(1L);
        assertEquals(1, activeCards.size());
        assertEquals(activeCard.getCardholderId(), activeCards.get(0).getCardholderId());
    }

    @Test
    void shouldReturnEmptyList_whenCardholderIdHasNoActiveCards() {
        CardholderCard inactiveCard = new CardholderCard();
        inactiveCard.setCardholderId(2L);
        inactiveCard.setActive(false);
        entityManager.persist(inactiveCard);
        entityManager.flush();

        List<CardholderCard> activeCards = cardholderCardRepository.findByCardholderIdAndActiveTrue(2L);
        assertTrue(activeCards.isEmpty());
    }
}