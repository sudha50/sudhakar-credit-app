package com.cardoffers.oms.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.test.context.ActiveProfiles;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import com.cardoffers.oms.model.entity.Offer;
import com.cardoffers.oms.repository.OfferRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Example;
import static org.junit.jupiter.api.Assertions.*;
import com.cardoffers.oms.model.entity.CardNetwork;
import com.cardoffers.oms.model.entity.Merchant;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(properties = "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect")
@ActiveProfiles("test")
class OfferRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OfferRepository offerRepository;

    @Test
void shouldFindActiveOffers_returnsResult() {
    // Given
    Merchant merchant = new Merchant();
    merchant.setName("Test Merchant");
    merchant.setTitle("title");
    merchant.setCreatedAt(java.time.LocalDateTime.now());
    merchant.setUpdatedAt(java.time.LocalDateTime.now());
    entityManager.persist(merchant);
    
    CardNetwork cardNetwork = new CardNetwork();
    cardNetwork.setName("Test CardNetwork");
    cardNetwork.setTitle("title");
    cardNetwork.setCreatedAt(java.time.LocalDateTime.now());
    cardNetwork.setUpdatedAt(java.time.LocalDateTime.now());
    entityManager.persist(cardNetwork);
    
    Offer offer = new Offer();
    offer.setTitle("Test Offer");
    offer.setMerchant(merchant);
    offer.setCardNetwork(cardNetwork);
    offer.setActive(true);
    offer.setStartDate(LocalDate.now().minusDays(1));
    offer.setEndDate(LocalDate.now().plusDays(1));
    offer.setCreatedAt(java.time.LocalDateTime.now());
    offer.setUpdatedAt(java.time.LocalDateTime.now());
    entityManager.persistAndFlush(offer);
    
    // When
    List<Offer> result = offerRepository.findActiveOffers(LocalDate.now());

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Test Offer", result.get(0).getTitle());
}

    @Test
void shouldFindActiveOffers_returnsEmpty() {
    // Given
    LocalDate currentDate = LocalDate.now();
    
    // When
    List<Offer> result = offerRepository.findActiveOffers(currentDate);
    
    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
}

    @Test
void shouldFindActiveOffersByMerchant_returnsResult() {
    // Given
    Merchant merchant = new Merchant();
    merchant.setName("Merchant A");
    merchant.setTitle("title");
    merchant.setCreatedAt(java.time.LocalDateTime.now());
    merchant.setUpdatedAt(java.time.LocalDateTime.now());
    entityManager.persist(merchant);

    CardNetwork cardNetwork = new CardNetwork();
    cardNetwork.setName("Visa");
    cardNetwork.setTitle("title");
    cardNetwork.setCreatedAt(java.time.LocalDateTime.now());
    cardNetwork.setUpdatedAt(java.time.LocalDateTime.now());
    entityManager.persist(cardNetwork);

    Offer offer = new Offer();
    offer.setTitle("10% Off");
    offer.setMerchant(merchant);
    offer.setCardNetwork(cardNetwork);
    offer.setActive(true);
    offer.setCreatedAt(java.time.LocalDateTime.now());
    offer.setUpdatedAt(java.time.LocalDateTime.now());
    entityManager.persist(offer);
    entityManager.flush();

    // When
    List<Offer> result = offerRepository.findActiveOffersByMerchant(merchant.getId());

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("10% Off", result.get(0).getTitle());
}

    @Test
void shouldFindActiveOffersByMerchant_returnsEmpty() {
    // Given
    Long merchantId = 1L;

    // When
    List<Offer> result = offerRepository.findActiveOffersByMerchant(merchantId);

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
}

    @Test
void shouldFindActiveOffersByCardNetwork_returnsResult() {
    // Given
    Merchant merchant = new Merchant();
    merchant.setName("Test Merchant");
    merchant.setTitle("title");
    merchant.setCreatedAt(java.time.LocalDateTime.now());
    merchant.setUpdatedAt(java.time.LocalDateTime.now());
    entityManager.persistAndFlush(merchant);

    CardNetwork cardNetwork = new CardNetwork();
    cardNetwork.setName("Visa");
    cardNetwork.setTitle("title");
    cardNetwork.setCreatedAt(java.time.LocalDateTime.now());
    cardNetwork.setUpdatedAt(java.time.LocalDateTime.now());
    entityManager.persistAndFlush(cardNetwork);

    Offer offer = new Offer();
    offer.setTitle("Special Offer");
    offer.setMerchant(merchant);
    offer.setCardNetwork(cardNetwork);
    offer.setActive(true);
    offer.setStartDate(LocalDate.now().minusDays(1));
    offer.setEndDate(LocalDate.now().plusDays(1));
    offer.setCreatedAt(java.time.LocalDateTime.now());
    offer.setUpdatedAt(java.time.LocalDateTime.now());
    entityManager.persistAndFlush(offer);

    // When
    List<Offer> result = offerRepository.findActiveOffersByCardNetwork(cardNetwork.getId());

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Special Offer", result.get(0).getTitle());
}

    @Test
void shouldFindActiveOffersByCardNetwork_returnsEmpty() {
    // Given
    Long networkId = 1L; // Example network ID that does not exist in the database

    // When
    List<Offer> result = offerRepository.findActiveOffersByCardNetwork(networkId);

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty(), "Expected empty result when no offers exist for the given card network");
}

}