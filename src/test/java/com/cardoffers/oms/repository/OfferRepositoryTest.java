package com.cardoffers.oms.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.cardoffers.oms.model.entity.Offer;
import com.cardoffers.oms.model.entity.Merchant;
import com.cardoffers.oms.model.entity.CardNetwork;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class OfferRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    OfferRepository offerRepository;

    @Test
    void shouldReturnActiveOffers_whenCurrentDateIsWithinRange() {
        // Arrange
        LocalDate currentDate = LocalDate.now();
        Offer offer = new Offer();
        offer.setActive(true);
        offer.setStartDate(currentDate.minusDays(1));
        offer.setEndDate(currentDate.plusDays(1));
        entityManager.persist(offer);
        entityManager.flush();

        // Act
        List<Offer> activeOffers = offerRepository.findActiveOffers(currentDate);

        // Assert
        assertNotNull(activeOffers);
        assertEquals(1, activeOffers.size());
        assertEquals(offer, activeOffers.get(0));
    }

    @Test
    void shouldReturnNoActiveOffers_whenNoOffersAreActive() {
        // Arrange
        LocalDate currentDate = LocalDate.now();

        // Act
        List<Offer> activeOffers = offerRepository.findActiveOffers(currentDate);

        // Assert
        assertNotNull(activeOffers);
        assertTrue(activeOffers.isEmpty());
    }

    @Test
    void shouldReturnActiveOffersByMerchant_whenValidMerchantId() {
        // Arrange
        Merchant merchant = new Merchant();
        entityManager.persist(merchant);
        entityManager.flush();

        Offer offer = new Offer();
        offer.setActive(true);
        offer.setMerchant(merchant);
        entityManager.persist(offer);
        entityManager.flush();

        // Act
        List<Offer> activeOffers = offerRepository.findActiveOffersByMerchant(merchant.getId());

        // Assert
        assertNotNull(activeOffers);
        assertEquals(1, activeOffers.size());
        assertEquals(offer, activeOffers.get(0));
    }

    @Test
    void shouldReturnNoActiveOffersByMerchant_whenInvalidMerchantId() {
        // Arrange
        Merchant merchant = new Merchant();
        entityManager.persist(merchant);
        entityManager.flush();

        // Act
        List<Offer> activeOffers = offerRepository.findActiveOffersByMerchant(999L);

        // Assert
        assertNotNull(activeOffers);
        assertTrue(activeOffers.isEmpty());
    }

    @Test
    void shouldReturnActiveOffersByCardNetwork_whenValidNetworkId() {
        // Arrange
        CardNetwork cardNetwork = new CardNetwork();
        entityManager.persist(cardNetwork);
        entityManager.flush();

        Offer offer = new Offer();
        offer.setActive(true);
        offer.setCardNetwork(cardNetwork);
        entityManager.persist(offer);
        entityManager.flush();

        // Act
        List<Offer> activeOffers = offerRepository.findActiveOffersByCardNetwork(cardNetwork.getId());

        // Assert
        assertNotNull(activeOffers);
        assertEquals(1, activeOffers.size());
        assertEquals(offer, activeOffers.get(0));
    }

    @Test
    void shouldReturnNoActiveOffersByCardNetwork_whenInvalidNetworkId() {
        // Arrange
        CardNetwork cardNetwork = new CardNetwork();
        entityManager.persist(cardNetwork);
        entityManager.flush();

        // Act
        List<Offer> activeOffers = offerRepository.findActiveOffersByCardNetwork(999L);

        // Assert
        assertNotNull(activeOffers);
        assertTrue(activeOffers.isEmpty());
    }
}