package com.cardoffers.oms.repository;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import com.cardoffers.oms.model.entity.CardholderCard;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.assertThat;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
class CardholderCardRepositoryFunctionalTest {

    @Autowired
    private CardholderCardRepository cardholderCardRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldFindByCardholderId() {
        // Given
        CardholderCard card1 = new CardholderCard();
        card1.setCardholderId(1L);
        card1.setActive(true);
        entityManager.persist(card1);
        
        CardholderCard card2 = new CardholderCard();
        card2.setCardholderId(1L);
        card2.setActive(false);
        entityManager.persist(card2);

        entityManager.flush();
        entityManager.clear();

        // When
        List<CardholderCard> result = cardholderCardRepository.findByCardholderId(1L);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(CardholderCard::getCardholderId).contains(1L);
    }

    @Test
    void shouldFindByCardholderIdAndActiveTrue() {
        // Given
        CardholderCard activeCard = new CardholderCard();
        activeCard.setCardholderId(1L);
        activeCard.setActive(true);
        entityManager.persist(activeCard);

        CardholderCard inactiveCard = new CardholderCard();
        inactiveCard.setCardholderId(1L);
        inactiveCard.setActive(false);
        entityManager.persist(inactiveCard);

        entityManager.flush();
        entityManager.clear();

        // When
        List<CardholderCard> result = cardholderCardRepository.findByCardholderIdAndActiveTrue(1L);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).isActive()).isTrue();
    }

    @Test
    void shouldReturnEmptyListWhenNoCardholderCardsFound() {
        // Given
        entityManager.flush();
        entityManager.clear();

        // When
        List<CardholderCard> result = cardholderCardRepository.findByCardholderId(999L);

        // Then
        assertThat(result).isEmpty();
    }
}