package com.cardoffers.oms.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
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
    entityManager.persistAndFlush(merchant);
    
    CardNetwork cardNetwork = new CardNetwork();
    cardNetwork.setName("Test Network");
    cardNetwork.setTitle("title");
    cardNetwork.setCreatedAt(java.time.LocalDateTime.now());
    cardNetwork.setUpdatedAt(java.time.LocalDateTime.now());
    entityManager.persistAndFlush(cardNetwork);
    
    Offer offer = new Offer();
    offer.setTitle("Test Offer");
    offer.setActive(true);
    offer.setMerchant(merchant);
    offer.setCardNetwork(cardNetwork);
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
merchant.setName("Test Merchant");
    
    CardNetwork cardNetwork = new CardNetwork();
cardNetwork.setName("Visa");
    
    Offer offer = new Offer();
    offer.setTitle("10% Off");
    offer.setActive(true);
    offer.setMerchant(merchant);
    offer.setCardNetwork(cardNetwork);

    entityManager.persist(merchant);
    entityManager.persist(cardNetwork);
    entityManager.persistAndFlush(offer);

    // When
    List<Offer> result = offerRepository.findActiveOffersByMerchant(1L);

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("10% Off", result.get(0).getTitle());
}

    @Test
void shouldFindActiveOffersByMerchant_returnsEmpty() {
    // Given
    Long merchantId = 1L; // Example merchant ID that does not exist in the database

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
// Assuming ID is auto-generated in the database
    // Set other required Merchant fields here
    entityManager.persist(merchant);
    
    CardNetwork cardNetwork = new CardNetwork();
// Assuming ID is auto-generated in the database
    // Set other required CardNetwork fields here
    entityManager.persist(cardNetwork);
    
    Offer offer = new Offer();
    offer.setTitle("Special Offer");
    // Set other required Offer fields here
    offer.setMerchant(merchant);
    offer.setCardNetwork(cardNetwork);
    offer.setActive(true);
    entityManager.persistAndFlush(offer);

    // When
    List<Offer> result = offerRepository.findActiveOffersByCardNetwork(cardNetwork.getId());

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(offer.getTitle(), result.get(0).getTitle());
}

    @Test
void shouldFindActiveOffersByCardNetwork_returnsEmpty() {
    // Given
    Long networkId = 1L;

    // When
    List<Offer> result = offerRepository.findActiveOffersByCardNetwork(networkId);

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
}

}