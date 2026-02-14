package com.cardoffers.oms.service;

import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cardoffers.oms.exception.ResourceNotFoundException;
import com.cardoffers.oms.mapper.CardholderMapper;
import com.cardoffers.oms.mapper.CardNetworkMapper;
import com.cardoffers.oms.model.dto.CardholderDTO;
import com.cardoffers.oms.model.entity.Cardholder;
import com.cardoffers.oms.repository.CardholderCardRepository;
import com.cardoffers.oms.repository.CardholderRepository;

@ExtendWith(MockitoExtension.class)
class CardholderServiceImplTest {

    @Mock
    CardholderRepository cardholderRepository;

    @Mock
    CardholderCardRepository cardholderCardRepository;

    @Mock
    CardholderMapper cardholderMapper;

    @Mock
    CardNetworkMapper cardNetworkMapper;

    @InjectMocks
    CardholderServiceImpl cardholderServiceImpl;

    private static CardholderDTO aCardholderDTO() {
        CardholderDTO entity = new CardholderDTO();
        entity.setFirstName("John");
        entity.setLastName("Doe");
        entity.setEmail("test@example.com");
        entity.setPhoneNumber("+1234567890");
        entity.setActive(true);
        entity.setCardNetworks(Collections.emptyList());
        return entity;
    }

    private static Cardholder aCardholder() {
        Cardholder entity = new Cardholder();
        entity.setId(1L);
        entity.setFirstName("John");
        entity.setLastName("Doe");
        entity.setEmail("test@example.com");
        entity.setPhoneNumber("+1234567890");
        return entity;
    }

    @Test
    void shouldReturnCardholderDTO_whenGetCardholderById() {
        Cardholder cardholder = aCardholder();
        CardholderDTO cardholderDTO = aCardholderDTO();

        when(cardholderRepository.findById(1L)).thenReturn(Optional.of(cardholder));
        when(cardholderMapper.toDTO(cardholder)).thenReturn(cardholderDTO);

        CardholderDTO result = cardholderServiceImpl.getCardholderById(1L);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
    }

    @Test
    void shouldThrowResourceNotFoundException_whenGetCardholderById_notFound() {
        when(cardholderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cardholderServiceImpl.getCardholderById(1L));
    }

    @Test
    void shouldReturnCardholderDTO_whenGetCardholderByEmail() {
        Cardholder cardholder = aCardholder();
        CardholderDTO cardholderDTO = aCardholderDTO();

        when(cardholderRepository.findByEmail("test@example.com")).thenReturn(Optional.of(cardholder));
        when(cardholderMapper.toDTO(cardholder)).thenReturn(cardholderDTO);

        CardholderDTO result = cardholderServiceImpl.getCardholderByEmail("test@example.com");

        assertNotNull(result);
        assertEquals("Doe", result.getLastName());
    }

    @Test
    void shouldThrowResourceNotFoundException_whenGetCardholderByEmail_notFound() {
        when(cardholderRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cardholderServiceImpl.getCardholderByEmail("test@example.com"));
    }

    @Test
    void shouldCreateCardholderDTO_whenCreateCardholder() {
        CardholderDTO cardholderDTO = aCardholderDTO();
        Cardholder cardholder = aCardholder();

        when(cardholderMapper.toEntity(cardholderDTO)).thenReturn(cardholder);
        when(cardholderRepository.save(cardholder)).thenReturn(cardholder);
        when(cardholderMapper.toDTO(cardholder)).thenReturn(cardholderDTO);

        CardholderDTO result = cardholderServiceImpl.createCardholder(cardholderDTO);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
    }

    @Test
    void shouldUpdateCardholderDTO_whenUpdateCardholder() {
        CardholderDTO cardholderDTO = aCardholderDTO();
        Cardholder existingCardholder = aCardholder();

        when(cardholderRepository.findById(1L)).thenReturn(Optional.of(existingCardholder));
        when(cardholderRepository.save(existingCardholder)).thenReturn(existingCardholder);
        when(cardholderMapper.toDTO(existingCardholder)).thenReturn(cardholderDTO);

        CardholderDTO result = cardholderServiceImpl.updateCardholder(1L, cardholderDTO);

        assertNotNull(result);
        assertEquals("Doe", result.getLastName());
    }

    @Test
    void shouldThrowResourceNotFoundException_whenUpdateCardholder_notFound() {
        CardholderDTO cardholderDTO = aCardholderDTO();
        when(cardholderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cardholderServiceImpl.updateCardholder(1L, cardholderDTO));
    }
}