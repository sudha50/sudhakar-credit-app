package com.cardoffers.oms.service;

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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import com.cardoffers.oms.model.dto.CardholderDTO;
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
class CardholderServiceIntegrationTest {

    @Autowired
    private CardholderService cardholderService;

    @Test
void shouldCreateCardholder_happyPath() {
    // Given
    CardholderDTO cardholderDTO = new CardholderDTO();
    cardholderDTO.setFirstName("John");
    cardholderDTO.setLastName("Doe");
    cardholderDTO.setEmail("john.doe@example.com");
    cardholderDTO.setPhoneNumber("1234567890");
    cardholderDTO.setActive(true);

    // When
    CardholderDTO result = cardholderService.createCardholder(cardholderDTO);

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
void shouldUpdateCardholder_happyPath() {
    // Given
    CardholderDTO existingCardholder = new CardholderDTO();
    existingCardholder.setId(1L);
    existingCardholder.setFirstName("John");
    existingCardholder.setLastName("Doe");
    existingCardholder.setEmail("john.doe@example.com");
    
    CardholderDTO updatedCardholder = new CardholderDTO();
    updatedCardholder.setId(1L);
    updatedCardholder.setFirstName("Jane");
    updatedCardholder.setLastName("Doe");
    updatedCardholder.setEmail("jane.doe@example.com");


    // When
    CardholderDTO result = cardholderService.updateCardholder(1L, updatedCardholder);

    // Then
    assertNotNull(result);
    assertEquals("Jane", result.getFirstName());
    assertEquals("Doe", result.getLastName());
    assertEquals("jane.doe@example.com", result.getEmail());
}



    @Test
void shouldGetCardholderById_negativeId() {
    // Given
    Long negativeId = -1L;

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> cardholderService.getCardholderById(negativeId));
}



    @Test
void shouldGetAllActiveCardholders_emptyList() {
    // Given
    // No active cardholders in the system

    // When
    List<CardholderDTO> result = cardholderService.getAllActiveCardholders();

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
}

}