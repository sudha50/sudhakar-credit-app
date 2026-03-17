package com.cardoffers.oms.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import com.cardoffers.oms.model.entity.CardNetwork;
import com.cardoffers.oms.repository.CardNetworkRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(properties = "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect")
@ActiveProfiles("test")
class CardNetworkRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CardNetworkRepository cardNetworkRepository;

    @Test
void shouldFindByCode_returnsResult() {
    // Given
    CardNetwork cardNetwork = new CardNetwork();
    cardNetwork.setName("Visa");
    cardNetwork.setCode("VISA123");
    cardNetwork.setCreatedAt(LocalDateTime.now());
    cardNetwork.setActive(true);
    entityManager.persistAndFlush(cardNetwork);

    // When
    Optional<CardNetwork> result = cardNetworkRepository.findByCode("VISA123");

    // Then
    assertTrue(result.isPresent());
    assertEquals("Visa", result.get().getName());
    assertEquals("VISA123", result.get().getCode());
}

    @Test
void shouldFindByCode_returnsEmpty() {
    // Given
    String code = "NON_EXISTENT_CODE";

    // When
    Optional<CardNetwork> result = cardNetworkRepository.findByCode(code);

    // Then
    assertNotNull(result);
    assertFalse(result.isPresent());
}

    @Test
void shouldFindByActiveTrue_returnsResult() {
    // Given
    CardNetwork cardNetwork = new CardNetwork();
    cardNetwork.setName("Visa");
    cardNetwork.setCode("VISA");
    cardNetwork.setCreatedAt(LocalDateTime.now());
    cardNetwork.setActive(true);
    entityManager.persistAndFlush(cardNetwork);

    // When
    List<CardNetwork> result = cardNetworkRepository.findByActiveTrue();

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Visa", result.get(0).getName());
    assertEquals("VISA", result.get(0).getCode());
}

    @Test
void shouldFindByActiveTrue_returnsEmpty() {
    // When
    List<CardNetwork> result = cardNetworkRepository.findByActiveTrue();

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
}

}