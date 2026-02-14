package com.cardoffers.oms.service;

import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.cardoffers.oms.model.dto.CardholderDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardholderServiceTest {

    @InjectMocks 
    private CardholderService cardholderService;

    @Mock 
    private CardholderRepository cardholderRepository; // Assume this is the repository interface

    @Test
    void shouldReturnCardholder_whenCardholderExists() {
        CardholderDTO expectedCardholder = aCardholderDTO();
        expectedCardholder.setId(1L);
        when(cardholderRepository.findById(1L)).thenReturn(Optional.of(expectedCardholder));
        
        CardholderDTO actualCardholder = cardholderService.getCardholderById(1L);
        
        assertNotNull(actualCardholder);
        assertEquals(expectedCardholder.getId(), actualCardholder.getId());
        assertEquals(expectedCardholder.getFirstName(), actualCardholder.getFirstName());
    }

    @Test
    void shouldReturnNull_whenCardholderDoesNotExist() {
        when(cardholderRepository.findById(any())).thenReturn(Optional.empty());

        CardholderDTO actualCardholder = cardholderService.getCardholderById(1L);

        assertNull(actualCardholder);
    }

    @Test
    void shouldReturnCardholder_whenEmailExists() {
        CardholderDTO expectedCardholder = aCardholderDTO();
        expectedCardholder.setId(1L);
        when(cardholderRepository.findByEmail("test@example.com")).thenReturn(Optional.of(expectedCardholder));
        
        CardholderDTO actualCardholder = cardholderService.getCardholderByEmail("test@example.com");
        
        assertNotNull(actualCardholder);
        assertEquals(expectedCardholder.getEmail(), actualCardholder.getEmail());
    }

    @Test
    void shouldReturnNull_whenEmailDoesNotExist() {
        when(cardholderRepository.findByEmail(any())).thenReturn(Optional.empty());

        CardholderDTO actualCardholder = cardholderService.getCardholderByEmail("test@example.com");

        assertNull(actualCardholder);
    }

    @Test
    void shouldCreateCardholder_whenValidDataProvided() {
        CardholderDTO cardholderToCreate = aCardholderDTO();
        when(cardholderRepository.save(any(CardholderDTO.class))).thenReturn(cardholderToCreate);
        
        CardholderDTO createdCardholder = cardholderService.createCardholder(cardholderToCreate);
        
        assertNotNull(createdCardholder);
        assertEquals(cardholderToCreate.getFirstName(), createdCardholder.getFirstName());
    }

    @Test
    void shouldUpdateCardholder_whenValidIdProvided() {
        CardholderDTO existingCardholder = aCardholderDTO();
        existingCardholder.setId(1L);
        CardholderDTO updatedData = aCardholderDTO();
        updatedData.setFirstName("Jane");

        when(cardholderRepository.findById(1L)).thenReturn(Optional.of(existingCardholder));
        when(cardholderRepository.save(any(CardholderDTO.class))).thenReturn(updatedData);

        CardholderDTO updatedCardholder = cardholderService.updateCardholder(1L, updatedData);

        assertNotNull(updatedCardholder);
        assertEquals(updatedData.getFirstName(), updatedCardholder.getFirstName());
    }

    private static CardholderDTO aCardholderDTO() {
        CardholderDTO entity = new CardholderDTO();
        entity.setFirstName("John");
        entity.setLastName("Doe");
        entity.setEmail("test@example.com");
        entity.setPhoneNumber("+1234567890");
        entity.setActive(true);
        entity.setCardNetworks(null); // Set List here as required
        return entity;
    }
}