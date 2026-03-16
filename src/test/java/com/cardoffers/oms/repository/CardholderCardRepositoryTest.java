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
    cardholder.setId(1L); // Assuming setter for ID
    // Set other required fields for Cardholder
    entityManager.persistAndFlush(cardholder);

    CardNetwork cardNetwork = new CardNetwork();
    cardNetwork.setId(1L); // Assuming setter for ID
    // Set other required fields for CardNetwork
    entityManager.persistAndFlush(cardNetwork);

    CardholderCard cardholderCard = new CardholderCard();
    cardholderCard.setCardholder(cardholder);
    cardholderCard.setCardNetwork(cardNetwork);
    cardholderCard.setCardNumberLastFour("1234");
    cardholderCard.setCardType("VISA");
    cardholderCard.setCreatedAt(LocalDateTime.now());
    cardholderCard.setActive(true);
    entityManager.persistAndFlush(cardholderCard);

    // When
    List<CardholderCard> result = cardholderCardRepository.findByCardholderId(1L);

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("1234", result.get(0).getCardNumberLastFour());
    assertEquals("VISA", result.get(0).getCardType());
}

    @Test
void shouldFindByCardholderId_returnsEmpty() {
    // Given
    Long cardholderId = 1L; // Assuming we want to test with a specific cardholder ID

    // When
    List<CardholderCard> result = cardholderCardRepository.findByCardholderId(cardholderId);

    // Then
    assertTrue(result.isEmpty());
}

    @Test
void shouldFindByCardholderIdAndActiveTrue_returnsResult() {
    // Given
    Cardholder cardholder = new Cardholder();
    cardholder.setId(1L); // Assuming setId is part of the entity
    // Set all required fields for Cardholder here

    CardNetwork cardNetwork = new CardNetwork();
    cardNetwork.setId(1L); // Assuming setId is part of the entity
    // Set all required fields for CardNetwork here

    entityManager.persistAndFlush(cardholder);
    entityManager.persistAndFlush(cardNetwork);

    CardholderCard cardholderCard = new CardholderCard();
    cardholderCard.setCardholder(cardholder);
    cardholderCard.setCardNetwork(cardNetwork);
    cardholderCard.setCardNumberLastFour("1234");
    cardholderCard.setCardType("VISA");
    cardholderCard.setCreatedAt(LocalDateTime.now());
    cardholderCard.setActive(true);
    
    entityManager.persistAndFlush(cardholderCard);

    // When
    List<CardholderCard> result = cardholderCardRepository.findByCardholderIdAndActiveTrue(cardholder.getId());

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("1234", result.get(0).getCardNumberLastFour());
    assertNotNull(result.get(0).getActive());
    assertTrue(result.get(0).getActive());
}

    @Test
void shouldFindByCardholderIdAndActiveTrue_returnsEmpty() {
    // Given
    Long cardholderId = 1L;

    // When
    List<CardholderCard> result = cardholderCardRepository.findByCardholderIdAndActiveTrue(cardholderId);

    // Then
    assertTrue(result.isEmpty());
}

}