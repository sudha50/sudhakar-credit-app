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
import com.cardoffers.oms.model.entity.Merchant;
import com.cardoffers.oms.repository.MerchantRepository;
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
class MerchantRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MerchantRepository merchantRepository;

    @Test
void shouldFindByActiveTrue_returnsResult() {
    // Given
    Merchant merchant = new Merchant();
    merchant.setName("Active Merchant");
    merchant.setCategory("Retail");
    merchant.setCreatedAt(LocalDateTime.now());
    merchant.setActive(true);
    entityManager.persistAndFlush(merchant);

    // When
    List<Merchant> result = merchantRepository.findByActiveTrue();

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Active Merchant", result.get(0).getName());
}

    @Test
void shouldFindByActiveTrue_returnsEmpty() {
    // Given
    // No merchants exist in the database

    // When
    List<Merchant> result = merchantRepository.findByActiveTrue();

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
}

    @Test
void shouldFindByCategory_returnsResult() {
    // Given
    Merchant merchant = new Merchant();
    merchant.setName("Merchant A");
    merchant.setCategory("Electronics");
    merchant.setCreatedAt(LocalDateTime.now());
    merchant.setActive(true);
    entityManager.persistAndFlush(merchant);

    // When
    List<Merchant> result = merchantRepository.findByCategory("Electronics");

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Merchant A", result.get(0).getName());
}

    @Test
void shouldFindByCategory_returnsEmpty() {
    // Given
    String category = "nonexistentCategory";

    // When
    List<Merchant> result = merchantRepository.findByCategory(category);

    // Then
    assertTrue(result.isEmpty());
}

    @Test
void shouldFindByNameIgnoreCase_returnsResult() {
    // Given
    Merchant merchant = new Merchant();
    merchant.setName("Test Merchant");
    merchant.setCategory("Retail");
    merchant.setCreatedAt(LocalDateTime.now());
    merchant.setActive(true);
    entityManager.persistAndFlush(merchant);

    // When
    Optional<Merchant> result = merchantRepository.findByNameIgnoreCase("test merchant");

    // Then
    assertTrue(result.isPresent());
    assertEquals("Test Merchant", result.get().getName());
}

    @Test
void shouldFindByNameIgnoreCase_returnsEmpty() {
    // Given
    String name = "NonExistentMerchant";

    // When
    Optional<Merchant> result = merchantRepository.findByNameIgnoreCase(name);

    // Then
    assertTrue(result.isEmpty());
}

}