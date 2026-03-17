package com.cardoffers.oms.service;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.test.annotation.DirtiesContext;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import com.cardoffers.oms.model.entity.CardNetwork;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.cache.type=none",
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration,org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration"
    })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class CardholderServiceImplFunctionalTest {

    @MockBean
    private CardholderRepository cardholderRepository;

    @MockBean
    private CardholderCardRepository cardholderCardRepository;

    @MockBean
    private CardholderMapper cardholderMapper;

    @MockBean
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
    CardholderDTO inputDto = new CardholderDTO();
    inputDto.setFirstName("John");
    inputDto.setLastName("Doe");
    inputDto.setEmail("john.doe@example.com");
    inputDto.setPhoneNumber("1234567890");
    inputDto.setActive(true);
    
    CardholderDTO expectedDto = new CardholderDTO();
    expectedDto.setId(1L);
    expectedDto.setFirstName("John");
    expectedDto.setLastName("Doe");
    expectedDto.setEmail("john.doe@example.com");
    expectedDto.setPhoneNumber("1234567890");
    expectedDto.setActive(true);

    when(cardholderMapper.toEntity(any())).thenReturn(new Cardholder());
    when(cardholderRepository.save(any())).thenReturn(new Cardholder());
    when(cardholderMapper.toDTO(any())).thenReturn(expectedDto);

    // When
    CardholderDTO result = cardholderService.createCardholder(inputDto);

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
void shouldCreateCardholder_notFound() {
    // Given
    when(cardholderRepository.findByEmail(anyString())).thenReturn(Optional.empty());
    when(cardholderCardRepository.findByCardholderId(anyLong())).thenReturn(new ArrayList<>());
    when(cardholderMapper.toEntity(any())).thenReturn(new Cardholder());
    when(cardNetworkMapper.toEntity(any())).thenReturn(new CardNetwork());

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
    Cardholder cardholder = new Cardholder();
    cardholder.setId(1L);
    cardholder.setFirstName("John");
    cardholder.setLastName("Doe");
    cardholder.setEmail("john.doe@example.com");
    cardholder.setPhoneNumber("1234567890");
    cardholder.setActive(true);
    when(cardholderRepository.findById(1L)).thenReturn(Optional.of(cardholder));

    Cardholder updatedCardholder = new Cardholder();
    updatedCardholder.setId(1L);
    updatedCardholder.setFirstName("John");
    updatedCardholder.setLastName("Doe");
    updatedCardholder.setEmail("john.doe@example.com");
    updatedCardholder.setPhoneNumber("9876543210");
    updatedCardholder.setActive(true);
    when(cardholderRepository.save(any())).thenReturn(updatedCardholder);

    CardholderDTO cardholderDTO = new CardholderDTO();
    cardholderDTO.setId(1L);
    cardholderDTO.setFirstName("John");
    cardholderDTO.setLastName("Doe");
    cardholderDTO.setEmail("john.doe@example.com");
    cardholderDTO.setPhoneNumber("9876543210");
    cardholderDTO.setActive(true);
    when(cardholderMapper.toEntity(any())).thenReturn(cardholder);
    when(cardholderMapper.toDTO(any())).thenReturn(cardholderDTO);

    // When
    CardholderDTO result = cardholderService.updateCardholder(1L, cardholderDTO);

    // Then
    assertNotNull(result);
    assertEquals("John", result.getFirstName());
    assertEquals("Doe", result.getLastName());
    assertEquals("john.doe@example.com", result.getEmail());
    assertEquals("9876543210", result.getPhoneNumber());
    assertNotNull(result.getActive());
    assertTrue(result.getActive());
}

    @Test
void shouldUpdateCardholder_notFound() {
    // Given
    Long nonExistentId = 1L;
    CardholderDTO cardholderDTO = new CardholderDTO();
    cardholderDTO.setFirstName("John");
    cardholderDTO.setLastName("Doe");
    cardholderDTO.setEmail("john.doe@example.com");

    when(cardholderRepository.findById(anyLong())).thenReturn(Optional.empty());
    when(cardholderCardRepository.findByCardholderId(anyLong())).thenReturn(new ArrayList<>());
    when(cardholderMapper.toEntity(any())).thenReturn(new Cardholder());
    when(cardholderMapper.toDTO(any())).thenReturn(cardholderDTO);

    // When / Then
    assertThrows(ResourceNotFoundException.class, () -> {
        // CRITICAL: Read-Modify-Write pattern required.
        // Save the complete entity FIRST (all @NotNull fields), then call update.
        // The service should load the existing entity from DB before updating to preserve createdAt.
        // Pattern: EntityType saved = repository.save(new EntityType(...all fields...));
        //           UpdateDTO dto = new UpdateDTO(saved.getId(), "new value");
        //           service.update(dto); // createdAt preserved via DB read
        cardholderService.updateCardholder(nonExistentId, cardholderDTO);
    });
}

    @Test
void shouldGetCardholderById_nullId() {
    // Given
    Long id = null;

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> cardholderService.getCardholderById(id));
}

    @Test
void shouldGetCardholderByEmail_emptyEmail() {
    // Given
    String email = "";

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> cardholderService.getCardholderByEmail(email));
}

}