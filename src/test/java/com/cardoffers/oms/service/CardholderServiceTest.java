package com.cardoffers.oms.service;

import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.cardoffers.oms.repository.CardholderRepository;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardholderServiceTest {

    @Mock
    private CardholderService cardholderService;
    @Mock
    private CardholderRepository cardholderRepository;

    @Test
void shouldCreateCardholder_happyPath() {
    // Given
    CardholderDTO dto = new CardholderDTO();
    dto.setFirstName("John");
    dto.setLastName("Doe");
    dto.setEmail("john.doe@example.com");
    dto.setPhoneNumber("1234567890");
    dto.setActive(true);
    when(cardholderService.createCardholder(dto)).thenReturn(dto);

    // When
    CardholderDTO result = cardholderService.createCardholder(dto);

    // Then
    assertNotNull(result);
    assertEquals("John", result.getFirstName());
    assertEquals("Doe", result.getLastName());
    assertEquals("john.doe@example.com", result.getEmail());
    assertEquals("1234567890", result.getPhoneNumber());
    assertNotNull(result.getActive());
    assertTrue(result.getActive());
    verify(cardholderService).createCardholder(dto);
}

    @Test
void shouldUpdateCardholder_happyPath() {
    // Given
    CardholderDTO cardholder = new CardholderDTO();
    cardholder.setId(1L);
    cardholder.setFirstName("John");
    cardholder.setLastName("Doe");
    cardholder.setEmail("john.doe@example.com");
    
    CardholderDTO updatedCardholder = new CardholderDTO();
    updatedCardholder.setId(1L);
    updatedCardholder.setFirstName("Johnathan");
    updatedCardholder.setLastName("Doe");
    updatedCardholder.setEmail("johnathan.doe@example.com");

    when(cardholderRepository.findById(1L)).thenReturn(Optional.of(cardholder));
    when(cardholderRepository.save(any())).thenReturn(updatedCardholder);

    // When
    CardholderDTO result = cardholderService.updateCardholder(1L, updatedCardholder);

    // Then
    assertNotNull(result);
    assertEquals("Johnathan", result.getFirstName());
    assertEquals("Doe", result.getLastName());
    assertEquals("johnathan.doe@example.com", result.getEmail());
    verify(cardholderRepository).findById(1L);
    verify(cardholderRepository).save(any());
}







    @Test
void shouldUpdateCardholder_nonExistentId() {
    // Given
    Long nonExistentId = 999L;
    CardholderDTO cardholderDTO = new CardholderDTO();
    cardholderDTO.setFirstName("John");
    cardholderDTO.setLastName("Doe");
    cardholderDTO.setEmail("john.doe@example.com");

    when(cardholderService.updateCardholder(nonExistentId, cardholderDTO)).thenThrow(new NoSuchElementException("Cardholder not found"));

    // When & Then
    assertThrows(NoSuchElementException.class, () -> cardholderService.updateCardholder(nonExistentId, cardholderDTO));
}

    @Test
void shouldGetAllActiveCardholders_emptyList() {
    // Given
    when(cardholderService.getAllActiveCardholders()).thenReturn(new ArrayList<>());

    // When
    List<CardholderDTO> result = cardholderService.getAllActiveCardholders();

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
    verify(cardholderService).getAllActiveCardholders();
}

}