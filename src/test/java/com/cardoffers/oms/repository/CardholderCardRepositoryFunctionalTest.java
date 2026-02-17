package com.cardoffers.oms.repository;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import static org.assertj.core.api.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import com.cardoffers.oms.model.entity.CardholderCard;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
class CardholderCardRepositoryFunctionalTest {

    @Autowired
    private CardholderCardRepository cardholderCardRepository;

    @Test
    void shouldReturnCardsByCardholderId() {
        // given
        CardholderCard card1 = new CardholderCard();
        card1.setCardholderId(1L);
        card1.setActive(true);
        cardholderCardRepository.save(card1);

        CardholderCard card2 = new CardholderCard();
        card2.setCardholderId(1L);
        card2.setActive(false);
        cardholderCardRepository.save(card2);

        CardholderCard card3 = new CardholderCard();
        card3.setCardholderId(2L);
        card3.setActive(true);
        cardholderCardRepository.save(card3);

        // when
        List<CardholderCard> result = cardholderCardRepository.findByCardholderId(1L);

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    void shouldReturnActiveCardsByCardholderId() {
        // given
        CardholderCard card1 = new CardholderCard();
        card1.setCardholderId(1L);
        card1.setActive(true);
        cardholderCardRepository.save(card1);

        CardholderCard card2 = new CardholderCard();
        card2.setCardholderId(1L);
        card2.setActive(false);
        cardholderCardRepository.save(card2);

        // when
        List<CardholderCard> result = cardholderCardRepository.findByCardholderIdAndActiveTrue(1L);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).isActive()).isTrue();
    }

    @Test
    void shouldReturnEmptyListWhenNoCardsFoundByCardholderId() {
        // when
        List<CardholderCard> result = cardholderCardRepository.findByCardholderId(999L);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnEmptyListWhenNoActiveCardsFoundByCardholderId() {
        // given
        CardholderCard card1 = new CardholderCard();
        card1.setCardholderId(1L);
        card1.setActive(false);
        cardholderCardRepository.save(card1);

        // when
        List<CardholderCard> result = cardholderCardRepository.findByCardholderIdAndActiveTrue(1L);

        // then
        assertThat(result).isEmpty();
    }
}