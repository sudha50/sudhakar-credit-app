package com.cardoffers.oms.service;

import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import com.cardoffers.oms.exception.ResourceNotFoundException;
import com.cardoffers.oms.mapper.CardNetworkMapper;
import com.cardoffers.oms.mapper.CardholderMapper;
import com.cardoffers.oms.model.dto.CardNetworkDTO;
import com.cardoffers.oms.model.dto.CardholderDTO;
import com.cardoffers.oms.model.entity.Cardholder;
import com.cardoffers.oms.repository.CardholderCardRepository;
import com.cardoffers.oms.repository.CardholderRepository;
import com.cardoffers.oms.service.CardholderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(exclude = {RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class, CacheAutoConfiguration.class})
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.cache.type=none"})
class CardholderServiceImplIntegrationTest {

    @Autowired
    private CardholderRepository cardholderRepository;

    @Autowired
    private CardholderCardRepository cardholderCardRepository;

    @Autowired
    private CardholderMapper cardholderMapper;

    @Autowired
    private CardNetworkMapper cardNetworkMapper;

    @Autowired
    private CardholderService cardholderService;

    @BeforeEach
    void setUp() {
        // P16-B2 / S-27: Topological save strategy — parents MUST be saved before children
        // Save order: Cardholder
        // CRITICAL: this.cardholder is a class-level instance field.
        // Test methods MUST use this.cardholder — do NOT create a new Cardholder() inline.

        Cardholder cardholder = new Cardholder();
        cardholder.setFirstName("testFirstname");
        cardholder.setLastName("testLastname");
        cardholder.setEmail("testEmail");
        cardholder.setCreatedAt(java.time.LocalDateTime.now());
        cardholder.setUpdatedAt(java.time.LocalDateTime.now());
        this.cardholder = cardholderRepository.save(cardholder);
    }

    @Test
void shouldCreateCardholder_happyPath() {
    // Given
    CardholderDTO request = new CardholderDTO();
    request.setFirstName("John");
    request.setLastName("Doe");
    request.setEmail("john.doe@example.com");
    
    CardholderDTO expectedResponse = new CardholderDTO();
    expectedResponse.setId(1L);
    expectedResponse.setFirstName("John");
    expectedResponse.setLastName("Doe");
    expectedResponse.setEmail("john.doe@example.com");
    

    // When
    CardholderDTO result = cardholderService.createCardholder(request);

    // Then
    assertNotNull(result);
    assertEquals("John", result.getFirstName());
    assertEquals("Doe", result.getLastName());
    assertEquals("john.doe@example.com", result.getEmail());
}

    @Test
void shouldCreateCardholder_notFound() {
    // Given
    CardholderDTO cardholderDTO = new CardholderDTO();
    cardholderDTO.setFirstName("John");
    cardholderDTO.setLastName("Doe");
    cardholderDTO.setEmail("john.doe@example.com");


    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> cardholderService.createCardholder(cardholderDTO));
}

    @Test
void shouldUpdateCardholder_happyPath() {
    // Given
    CardholderDTO cardholderDTO = new CardholderDTO();
    cardholderDTO.setId(1L);
    cardholderDTO.setFirstName("John");
    cardholderDTO.setLastName("Doe");
    cardholderDTO.setEmail("john.doe@example.com");
    cardholderDTO.setActive(true);
    
    CardholderDTO updatedCardholder = new CardholderDTO();
    updatedCardholder.setId(1L);
    updatedCardholder.setFirstName("John");
    updatedCardholder.setLastName("Doe");
    updatedCardholder.setEmail("john.doe@example.com");
    updatedCardholder.setActive(true);


    // When
    CardholderDTO result = cardholderService.updateCardholder(1L, cardholderDTO);

    // Then
    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals("John", result.getFirstName());
    assertEquals("Doe", result.getLastName());
    assertEquals("john.doe@example.com", result.getEmail());
    assertNotNull(result.getActive());
    assertTrue(result.getActive());
}

    @Test
void shouldUpdateCardholder_notFound() {
    // Given

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> {
        cardholderService.updateCardholder(1L, new CardholderDTO());
    });
}



    @Test
void shouldGetCardholderById_nonExistentId() {
    // Given
    Long nonExistentId = 999L;

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> cardholderService.getCardholderById(nonExistentId));
}

}