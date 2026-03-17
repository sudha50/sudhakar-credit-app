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
import com.cardoffers.oms.model.entity.CardholderCard;
import com.cardoffers.oms.repository.CardholderCardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.junit.jupiter.api.Assertions.*;
import com.cardoffers.oms.model.entity.CardNetwork;
import com.cardoffers.oms.model.entity.Cardholder;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(properties = "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect")
@ActiveProfiles("test")
class CardholderCardRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CardholderCardRepository cardholderCardRepository;

    @Test
void shouldFindByCardholderId_returnsResult() {
    // Given
    Cardholder cardholder = new Cardholder();
// Assuming ID will be generated after persist
    // Set other required fields on Cardholder

    CardNetwork cardNetwork = new CardNetwork();
// Assuming ID will be generated after persist
    // Set other required fields on CardNetwork

    entityManager.persist(cardholder);
    entityManager.persist(cardNetwork);

    CardholderCard cardholderCard = new CardholderCard();
    cardholderCard.setCardholder(cardholder);
    cardholderCard.setCardNetwork(cardNetwork);
    cardholderCard.setCardNumberLastFour("1234");
    cardholderCard.setCardType("Visa");
    cardholderCard.setCreatedAt(LocalDateTime.now());
    cardholderCard.setActive(true);

    entityManager.persistAndFlush(cardholderCard);

    // When
    List<CardholderCard> result = cardholderCardRepository.findByCardholderId(cardholder.getId());

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("1234", result.get(0).getCardNumberLastFour());
    assertEquals("Visa", result.get(0).getCardType());
}

    @Test
void shouldFindByCardholderId_returnsEmpty() {
    // Given
    Long cardholderId = 1L; // Simulate a cardholder ID that does not exist

    // When
    List<CardholderCard> result = cardholderCardRepository.findByCardholderId(cardholderId);

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
}

    @Test
void shouldFindByCardholderIdAndActiveTrue_returnsResult() {
    // Given
    Cardholder cardholder = new Cardholder();
// Assuming ID is set manually for testing
    // Set other required fields for Cardholder here
    entityManager.persist(cardholder);

    CardNetwork cardNetwork = new CardNetwork();
// Assuming ID is set manually for testing
    // Set other required fields for CardNetwork here
    entityManager.persist(cardNetwork);

    CardholderCard cardholderCard = new CardholderCard();
    cardholderCard.setCardholder(cardholder);
    cardholderCard.setCardNetwork(cardNetwork);
    cardholderCard.setCardNumberLastFour("1234");
    cardholderCard.setCardType("Visa");
    cardholderCard.setCreatedAt(LocalDateTime.now());
    cardholderCard.setActive(true);
    entityManager.persistAndFlush(cardholderCard);

    // When
    List<CardholderCard> result = cardholderCardRepository.findByCardholderIdAndActiveTrue(1L);

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("1234", result.get(0).getCardNumberLastFour());
    assertEquals("Visa", result.get(0).getCardType());
}

    @Test
void shouldFindByCardholderIdAndActiveTrue_returnsEmpty() {
    // Given
    Long cardholderId = 1L;

    // When
    List<CardholderCard> result = cardholderCardRepository.findByCardholderIdAndActiveTrue(cardholderId);

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
}

}