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

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.cache.type=none",
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration,org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration"
    })
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
    CardholderDTO input = new CardholderDTO();
    input.setFirstName("John");
    input.setLastName("Doe");
    input.setEmail("john.doe@example.com");
    input.setActive(true);
    
    Cardholder expectedCardholder = new Cardholder();
    expectedCardholder.setId(1L);
    expectedCardholder.setFirstName("John");
    expectedCardholder.setLastName("Doe");
    expectedCardholder.setEmail("john.doe@example.com");
    expectedCardholder.setActive(true);
    
    
    // When
    CardholderDTO result = cardholderService.createCardholder(input);
    
    // Then
    assertNotNull(result);
    assertEquals("John", result.getFirstName());
    assertEquals("Doe", result.getLastName());
    assertEquals("john.doe@example.com", result.getEmail());
    assertNotNull(result.getActive());
    assertTrue(result.getActive());
}

    @Test
void shouldCreateCardholder_notFound() {
    // Given
    CardholderDTO dto = new CardholderDTO();
    dto.setFirstName("John");
    dto.setLastName("Doe");
    dto.setEmail("john.doe@example.com");
    

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> cardholderService.createCardholder(dto));
}

    @Test
void shouldUpdateCardholder_happyPath() {
    // Given
    CardholderDTO updatedCardholderDTO = new CardholderDTO();
    updatedCardholderDTO.setId(1L);
    updatedCardholderDTO.setFirstName("John");
    updatedCardholderDTO.setLastName("Doe");
    updatedCardholderDTO.setEmail("john.doe@example.com");
    updatedCardholderDTO.setPhoneNumber("1234567890");
    updatedCardholderDTO.setActive(true);

    Cardholder existingCardholder = new Cardholder();
    existingCardholder.setId(1L);
    existingCardholder.setFirstName("John");
    existingCardholder.setLastName("Doe");
    existingCardholder.setEmail("john.doe@example.com");
    existingCardholder.setPhoneNumber("1234567890");
    existingCardholder.setActive(true);
    

    // When
    CardholderDTO result = cardholderService.updateCardholder(1L, updatedCardholderDTO);

    // Then
    assertNotNull(result);
    assertEquals("John", result.getFirstName());
    assertEquals("Doe", result.getLastName());
    assertEquals("john.doe@example.com", result.getEmail());
    assertEquals("1234567890", result.getPhoneNumber());
    assertNotNull(result.getActive());
    assertTrue(result.getActive());
}

    @Test
void shouldUpdateCardholder_notFound() {
    // Given
    Long nonExistentId = 1L;
    CardholderDTO dto = new CardholderDTO();
    dto.setFirstName("John");
    dto.setLastName("Doe");
    dto.setEmail("john.doe@example.com");
    
    
    // When & Then
    assertThrows(ResourceNotFoundException.class, () ->        // CRITICAL: Read-Modify-Write pattern required.
        // Save the complete entity FIRST (all @NotNull fields), then call update.
        // The service should load the existing entity from DB before updating to preserve createdAt.
        // Pattern: EntityType saved = repository.save(new EntityType(...all fields...));
        //           UpdateDTO dto = new UpdateDTO(saved.getId(), "new value");
        //           service.update(dto); // createdAt preserved via DB read
 cardholderService.updateCardholder(nonExistentId, dto));
}

    @Test
void shouldGetCardholderById_nullId() {
    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> cardholderService.getCardholderById(null));
}

    @Test
void shouldGetCardholderByEmail_emptyEmail() {
    // Given
    String email = "";

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> cardholderService.getCardholderByEmail(email));
}

}