package com.cardoffers.oms.repository;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import static org.assertj.core.api.assertThat;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.cardoffers.oms.model.entity.CardholderCard;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class CardholderCardRepositoryFunctionalTest {

    @Autowired
    private CardholderCardRepository cardholderCardRepository;

    @Test
    public void shouldFindByCardholderId() {
        CardholderCard card = new CardholderCard();
        card.setCardholderId(1L);
        card.setActive(true);
        cardholderCardRepository.save(card);

        List<CardholderCard> result = cardholderCardRepository.findByCardholderId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCardholderId()).isEqualTo(1L);
    }

    @Test
    public void shouldFindByCardholderIdAndActiveTrue() {
        CardholderCard activeCard = new CardholderCard();
        activeCard.setCardholderId(1L);
        activeCard.setActive(true);
        cardholderCardRepository.save(activeCard);

        CardholderCard inactiveCard = new CardholderCard();
        inactiveCard.setCardholderId(1L);
        inactiveCard.setActive(false);
        cardholderCardRepository.save(inactiveCard);

        List<CardholderCard> result = cardholderCardRepository.findByCardholderIdAndActiveTrue(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getActive()).isTrue();
    }

    @Test
    public void shouldReturnEmptyWhenNoCardsFoundForCardholderId() {
        List<CardholderCard> result = cardholderCardRepository.findByCardholderId(999L);

        assertThat(result).isEmpty();
    }

    @Test
    public void shouldReturnEmptyWhenNoActiveCardsFoundForCardholderId() {
        CardholderCard card = new CardholderCard();
        card.setCardholderId(1L);
        card.setActive(false);
        cardholderCardRepository.save(card);

        List<CardholderCard> result = cardholderCardRepository.findByCardholderIdAndActiveTrue(1L);

        assertThat(result).isEmpty();
    }
}