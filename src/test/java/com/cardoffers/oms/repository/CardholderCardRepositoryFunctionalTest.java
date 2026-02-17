package com.cardoffers.oms.repository;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import com.cardoffers.oms.model.entity.CardholderCard;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
public class CardholderCardRepositoryFunctionalTest {

    @Autowired
    private CardholderCardRepository cardholderCardRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void shouldFindByCardholderId() {
        CardholderCard card1 = new CardholderCard();
        card1.setCardholderId(1L);
        card1.setActive(true);
        entityManager.persist(card1);

        CardholderCard card2 = new CardholderCard();
        card2.setCardholderId(1L);
        card2.setActive(false);
        entityManager.persist(card2);

        List<CardholderCard> result = cardholderCardRepository.findByCardholderId(1L);

        assertThat(result).hasSize(2);
        assertThat(result).extracting("active").containsExactlyInAnyOrder(true, false);
    }

    @Test
    public void shouldFindByCardholderIdAndActiveTrue() {
        CardholderCard card1 = new CardholderCard();
        card1.setCardholderId(1L);
        card1.setActive(true);
        entityManager.persist(card1);

        CardholderCard card2 = new CardholderCard();
        card2.setCardholderId(1L);
        card2.setActive(false);
        entityManager.persist(card2);

        List<CardholderCard> result = cardholderCardRepository.findByCardholderIdAndActiveTrue(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).isActive()).isTrue();
    }

    @Test
    public void shouldReturnEmptyListForNonExistentCardholderId() {
        List<CardholderCard> result = cardholderCardRepository.findByCardholderId(999L);

        assertThat(result).isEmpty();
    }
}