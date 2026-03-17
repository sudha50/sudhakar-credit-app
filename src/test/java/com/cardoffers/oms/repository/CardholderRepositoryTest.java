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
import com.cardoffers.oms.model.entity.Cardholder;
import com.cardoffers.oms.repository.CardholderRepository;
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
class CardholderRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CardholderRepository cardholderRepository;

    @Test
void shouldFindByActiveTrue_returnsResult() {
    // Given
    Cardholder cardholder = new Cardholder();
    cardholder.setFirstName("John");
    cardholder.setLastName("Doe");
    cardholder.setEmail("john.doe@example.com");
    cardholder.setCreatedAt(LocalDateTime.now());
    cardholder.setUpdatedAt(LocalDateTime.now());
    cardholder.setActive(true);
    entityManager.persistAndFlush(cardholder);

    // When
    List<Cardholder> result = cardholderRepository.findByActiveTrue();

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("john.doe@example.com", result.get(0).getEmail());
}

    @Test
void shouldFindByActiveTrue_returnsEmpty() {
    // Given
    // No Cardholder entity is persisted in the database

    // When
    List<Cardholder> result = cardholderRepository.findByActiveTrue();

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
}

}