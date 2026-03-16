package com.cardoffers.oms.service;

import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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
    
    when(cardholderMapper.toEntity(inputDto)).thenReturn(new Cardholder());
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
    CardholderDTO cardholderDTO = new CardholderDTO();
    cardholderDTO.setFirstName("John");
    cardholderDTO.setLastName("Doe");
    cardholderDTO.setEmail("john.doe@example.com");

    when(cardholderRepository.findByEmail(anyString())).thenReturn(Optional.empty());
    when(cardholderCardRepository.findByCardholderId(anyLong())).thenReturn(new ArrayList<>());
    when(cardholderMapper.toEntity(any())).thenReturn(new Cardholder());
    when(cardNetworkMapper.toEntity(any())).thenReturn(new CardNetwork());

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> cardholderService.createCardholder(cardholderDTO));
}

    @Test
void shouldUpdateCardholder_happyPath() {
    // Given
    Long cardholderId = 1L;
    CardholderDTO dto = new CardholderDTO();
    dto.setFirstName("John");
    dto.setLastName("Doe");
    dto.setEmail("john.doe@example.com");
    dto.setPhoneNumber("1234567890");
    dto.setActive(true);
    
    CardholderDTO updatedCardholder = new CardholderDTO();
    updatedCardholder.setId(cardholderId);
    updatedCardholder.setFirstName("John");
    updatedCardholder.setLastName("Doe");
    updatedCardholder.setEmail("john.doe@example.com");
    updatedCardholder.setPhoneNumber("1234567890");
    updatedCardholder.setActive(true);
    
    when(cardholderRepository.findById(anyLong())).thenReturn(Optional.of(new Cardholder()));
    when(cardholderRepository.save(any())).thenReturn(updatedCardholder);

    // When
    CardholderDTO result = cardholderService.updateCardholder(cardholderId, dto);

    // Then
    assertNotNull(result);
    assertEquals(cardholderId, result.getId());
    assertEquals("John", result.getFirstName());
    assertEquals("Doe", result.getLastName());
    assertEquals("john.doe@example.com", result.getEmail());
    assertEquals("1234567890", result.getPhoneNumber());
    assertNotNull(result.getActive());
    assertTrue(result.getActive());
    verify(cardholderRepository).findById(cardholderId);
    verify(cardholderRepository).save(any());
}

    @Test
void shouldUpdateCardholder_notFound() {
    // Given
    when(cardholderRepository.findById(anyLong())).thenReturn(Optional.empty());
    when(cardholderCardRepository.findByCardholderId(anyLong())).thenReturn(Collections.emptyList());
    when(cardholderMapper.toEntity(any())).thenReturn(new Cardholder());
    when(cardNetworkMapper.toEntity(any())).thenReturn(new CardNetwork());

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> cardholderService.updateCardholder(1L, new CardholderDTO()));
}



    @Test
void shouldGetCardholderById_invalidId() {
    // Given
    Long invalidId = 999L;
    when(cardholderRepository.findById(invalidId)).thenReturn(Optional.empty());

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> cardholderService.getCardholderById(invalidId));
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

}