package com.cardoffers.oms.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.cardoffers.oms.model.dto.CardholderDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class CardholderServiceTest {

    @Mock
    private CardholderService cardholderService;

    @InjectMocks
    private CardholderServiceImpl cardholderServiceImpl; // Assuming you will implement this service

    private CardholderDTO cardholderDTO;

    @BeforeEach
    void setUp() {
        cardholderDTO = new CardholderDTO(); // Populate with required fields
        cardholderDTO.setId(1L);
        cardholderDTO.setEmail("test@example.com");
        // Add other required fields
    }

    @Test
    void shouldReturnCardholder_whenValidIdProvided() {
        when(cardholderService.getCardholderById(1L)).thenReturn(cardholderDTO);

        CardholderDTO result = cardholderServiceImpl.getCardholderById(1L);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void shouldReturnCardholder_whenValidEmailProvided() {
        when(cardholderService.getCardholderByEmail("test@example.com")).thenReturn(cardholderDTO);

        CardholderDTO result = cardholderServiceImpl.getCardholderByEmail("test@example.com");

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void shouldReturnAllActiveCardholders_whenCalled() {
        when(cardholderService.getAllActiveCardholders()).thenReturn(Collections.singletonList(cardholderDTO));

        List<CardholderDTO> result = cardholderServiceImpl.getAllActiveCardholders();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnPersistedCardholder_whenCreating() {
        when(cardholderService.createCardholder(cardholderDTO)).thenReturn(cardholderDTO);

        CardholderDTO result = cardholderServiceImpl.createCardholder(cardholderDTO);

        assertNotNull(result);
        assertEquals(cardholderDTO.getId(), result.getId());
    }

    @Test
    void shouldUpdateCardholder_whenValidIdAndDtoProvided() {
        when(cardholderService.updateCardholder(1L, cardholderDTO)).thenReturn(cardholderDTO);

        CardholderDTO result = cardholderServiceImpl.updateCardholder(1L, cardholderDTO);

        assertNotNull(result);
        assertEquals(cardholderDTO.getEmail(), result.getEmail());
    }

    @Test
    void shouldThrowException_whenCardholderNotFound() {
        when(cardholderService.getCardholderById(99L)).thenThrow(new RuntimeException("Cardholder not found"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            cardholderServiceImpl.getCardholderById(99L);
        });

        assertEquals("Cardholder not found", exception.getMessage());
    }
}