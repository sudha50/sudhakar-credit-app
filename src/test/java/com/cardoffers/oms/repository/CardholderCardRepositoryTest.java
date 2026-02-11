package com.cardoffers.oms.repository;

import com.cardoffers.oms.model.entity.CardholderCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CardholderCardRepositoryTest {

    @Autowired
    private CardholderCardRepository cardholderCardRepository;

    private CardholderCard activeCard;
    private CardholderCard inactiveCard;

    @BeforeEach
    void setUp() {
        activeCard = new CardholderCard(1L, 100L, true);
        inactiveCard = new CardholderCard(2L, 100L, false);
        cardholderCardRepository.saveAll(Arrays.asList(activeCard, inactiveCard));
    }

    @Test
    void shouldFindByCardholderId_whenCardsExist() {
        List<CardholderCard> cards = cardholderCardRepository.findByCardholderId(100L);
        assertThat(cards).hasSize(2).containsExactlyInAnyOrder(activeCard, inactiveCard);
    }

    @Test
    void shouldReturnEmptyList_whenNoCardsExistForCardholderId() {
        List<CardholderCard> cards = cardholderCardRepository.findByCardholderId(200L);
        assertThat(cards).isEmpty();
    }

    @Test
    void shouldFindByCardholderIdAndActiveTrue_whenActiveCardExists() {
        List<CardholderCard> cards = cardholderCardRepository.findByCardholderIdAndActiveTrue(100L);
        assertThat(cards).hasSize(1).containsExactly(activeCard);
    }

    @Test
    void shouldReturnEmptyList_whenNoActiveCardsExistForCardholderId() {
        List<CardholderCard> cards = cardholderCardRepository.findByCardholderIdAndActiveTrue(200L);
        assertThat(cards).isEmpty();
    }

    @Test
    void shouldReturnEmptyList_whenAllCardsInactive() {
        List<CardholderCard> cards = cardholderCardRepository.findByCardholderIdAndActiveTrue(100L);
        assertThat(cards).doesNotContain(inactiveCard);
    }
}