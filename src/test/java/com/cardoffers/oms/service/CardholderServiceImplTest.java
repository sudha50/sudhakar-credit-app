package com.cardoffers.oms.service;

import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
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
import com.cardoffers.oms.service.CardholderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import com.cardoffers.oms.model.entity.CardNetwork;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CardholderServiceImplTest {

    @Mock
    private CardholderRepository cardholderRepository;

    @Mock
    private CardholderCardRepository cardholderCardRepository;

    @Mock
    private CardholderMapper cardholderMapper;

    @Mock
    private CardNetworkMapper cardNetworkMapper;

    @InjectMocks
    private CardholderServiceImpl cardholderService;

    @Test
void shouldCreateCardholder_happyPath() {
    // Given
    CardholderDTO cardholderDTO = new CardholderDTO();
    cardholderDTO.setFirstName("John");
    cardholderDTO.setLastName("Doe");
    cardholderDTO.setEmail("john.doe@example.com");
    cardholderDTO.setPhoneNumber("1234567890");
    cardholderDTO.setActive(true);

    Cardholder cardholder = new Cardholder();
    cardholder.setId(1L);
    cardholder.setFirstName("John");
    cardholder.setLastName("Doe");
    cardholder.setEmail("john.doe@example.com");
    cardholder.setPhoneNumber("1234567890");
    cardholder.setActive(true);

    when(cardholderMapper.toEntity(cardholderDTO)).thenReturn(cardholder);
    when(cardholderRepository.save(any())).thenReturn(cardholder);
    when(cardholderMapper.toDTO(cardholder)).thenReturn(cardholderDTO);

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
    verify(cardholderMapper).toEntity(cardholderDTO);
    verify(cardholderRepository).save(any());
    verify(cardholderMapper).toDTO(cardholder);
}

    @Test
void shouldCreateCardholder_notFound() {
    // Given
    when(cardholderRepository.findByEmail(any())).thenReturn(Optional.empty());
    when(cardholderCardRepository.findByCardholderId(anyLong())).thenReturn(List.of());
    when(cardholderMapper.toEntity(any())).thenReturn(new Cardholder());
    when(cardNetworkMapper.toEntity(any())).thenReturn(new CardNetwork());

    CardholderDTO dto = new CardholderDTO();
    dto.setFirstName("John");
    dto.setLastName("Doe");
    dto.setEmail("john.doe@example.com");

    // When / Then
    assertThrows(ResourceNotFoundException.class, () -> cardholderService.createCardholder(dto));
}

    @Test
void shouldUpdateCardholder_happyPath() {
    // Given
    Long id = 1L;
    CardholderDTO dto = new CardholderDTO();
    dto.setFirstName("John");
    dto.setLastName("Doe");
    dto.setEmail("john.doe@example.com");
    dto.setPhoneNumber("1234567890");

    Cardholder existingCardholder = new Cardholder();
    existingCardholder.setId(id);
    existingCardholder.setFirstName("Jane");
    existingCardholder.setLastName("Doe");
    existingCardholder.setEmail("jane.doe@example.com");
    existingCardholder.setPhoneNumber("0987654321");

    Cardholder updatedCardholder = new Cardholder();
    updatedCardholder.setId(id);
    updatedCardholder.setFirstName("John");
    updatedCardholder.setLastName("Doe");
    updatedCardholder.setEmail("john.doe@example.com");
    updatedCardholder.setPhoneNumber("1234567890");

    when(cardholderRepository.findById(id)).thenReturn(Optional.of(existingCardholder));
    when(cardholderRepository.save(any())).thenReturn(updatedCardholder);
    when(cardholderMapper.toEntity(dto)).thenReturn(updatedCardholder);
    when(cardholderMapper.toDTO(updatedCardholder)).thenReturn(dto);

    // When
    CardholderDTO result = cardholderService.updateCardholder(id, dto);

    // Then
    assertNotNull(result);
    assertEquals("John", result.getFirstName());
    assertEquals("Doe", result.getLastName());
    assertEquals("john.doe@example.com", result.getEmail());
    assertEquals("1234567890", result.getPhoneNumber());
    verify(cardholderRepository).findById(id);
    verify(cardholderRepository).save(any());
    verify(cardholderMapper).toEntity(dto);
    verify(cardholderMapper).toDTO(updatedCardholder);
}

    @Test
void shouldUpdateCardholder_notFound() {
    // Given
    Long cardholderId = 1L;
    CardholderDTO cardholderDTO = new CardholderDTO();
    cardholderDTO.setFirstName("John");
    cardholderDTO.setLastName("Doe");
    cardholderDTO.setEmail("john.doe@example.com");
    
    when(cardholderRepository.findById(anyLong())).thenReturn(Optional.empty());
    when(cardholderCardRepository.findByCardholderId(anyLong())).thenReturn(List.of());
    when(cardholderMapper.toEntity(any())).thenReturn(new Cardholder());
    when(cardNetworkMapper.toEntity(any())).thenReturn(new CardNetwork());
    
    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> 
        cardholderService.updateCardholder(cardholderId, cardholderDTO)
    );
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

    @Test
void shouldGetAllActiveCardholders_noActiveCardholders() {
    // Given
    when(cardholderRepository.findByActiveTrue()).thenReturn(Collections.emptyList());

    // When
    List<CardholderDTO> result = cardholderService.getAllActiveCardholders();

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
    verify(cardholderRepository).findByActiveTrue();
}

    @Test
void shouldCreateCardholder_maxLengthFields() {
    // Given
    CardholderDTO dto = new CardholderDTO();
    dto.setFirstName("A".repeat(255)); // assuming 255 is the max length
    dto.setLastName("B".repeat(255)); // assuming 255 is the max length
    dto.setEmail("test@example.com");
    
    when(cardholderMapper.toEntity(dto)).thenReturn(new Cardholder());
    when(cardholderRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
    when(cardholderRepository.save(any(Cardholder.class))).thenReturn(new Cardholder());

    // When
    CardholderDTO result = cardholderService.createCardholder(dto);

    // Then
    assertNotNull(result);
    assertEquals(dto.getFirstName(), result.getFirstName());
    assertEquals(dto.getLastName(), result.getLastName());
    assertEquals(dto.getEmail(), result.getEmail());
    verify(cardholderRepository).save(any(Cardholder.class));
}

}