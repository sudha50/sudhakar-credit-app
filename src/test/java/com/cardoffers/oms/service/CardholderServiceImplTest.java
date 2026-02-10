package com.cardoffers.oms.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cardoffers.oms.exception.ResourceNotFoundException;
import com.cardoffers.oms.mapper.CardholderMapper;
import com.cardoffers.oms.mapper.CardNetworkMapper;
import com.cardoffers.oms.model.dto.CardholderDTO;
import com.cardoffers.oms.model.dto.CardNetworkDTO;
import com.cardoffers.oms.model.entity.Cardholder;
import com.cardoffers.oms.repository.CardholderCardRepository;
import com.cardoffers.oms.repository.CardholderRepository;

@ExtendWith(MockitoExtension.class)
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

    private Cardholder cardholder;
    private CardholderDTO cardholderDTO;

    @BeforeEach
    void setUp() {
        cardholder = new Cardholder();
        cardholder.setId(1L);
        cardholder.setFirstName("John");
        cardholder.setLastName("Doe");
        cardholder.setEmail("john.doe@example.com");
        cardholder.setPhoneNumber("123456789");
        cardholder.setActive(true);

        cardholderDTO = new CardholderDTO();
        cardholderDTO.setId(1L);
        cardholderDTO.setFirstName("John");
        cardholderDTO.setLastName("Doe");
        cardholderDTO.setEmail("john.doe@example.com");
        cardholderDTO.setPhoneNumber("123456789");
        cardholderDTO.setActive(true);
    }

    @Test
    void shouldReturnCardholderDTO_whenGetCardholderByIdIsCalled() {
        when(cardholderRepository.findById(1L)).thenReturn(Optional.of(cardholder));
        when(cardholderMapper.toDTO(cardholder)).thenReturn(cardholderDTO);

        CardholderDTO result = cardholderService.getCardholderById(1L);
        
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(cardholderRepository).findById(1L);
    }

    @Test
    void shouldThrowResourceNotFoundException_whenCardholderNotFoundById() {
        when(cardholderRepository.findById(eq(1L))).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            cardholderService.getCardholderById(1L);
        });

        assertEquals("Cardholder not found", exception.getMessage());
    }

    @Test
    void shouldReturnCardholderDTO_whenGetCardholderByEmailIsCalled() {
        when(cardholderRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(cardholder));
        when(cardholderMapper.toDTO(cardholder)).thenReturn(cardholderDTO);

        CardholderDTO result = cardholderService.getCardholderByEmail("john.doe@example.com");
        
        assertNotNull(result);
        assertEquals("John Doe", result.getFirstName() + " " + result.getLastName());
        verify(cardholderRepository).findByEmail("john.doe@example.com");
    }

    @Test
    void shouldThrowResourceNotFoundException_whenCardholderNotFoundByEmail() {
        when(cardholderRepository.findByEmail(eq("john.doe@example.com"))).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            cardholderService.getCardholderByEmail("john.doe@example.com");
        });

        assertEquals("Cardholder not found", exception.getMessage());
    }

    @Test
    void shouldReturnAllActiveCardholders_whenGetAllActiveCardholdersIsCalled() {
        when(cardholderRepository.findByActiveTrue()).thenReturn(Collections.singletonList(cardholder));
        when(cardholderMapper.toDTO(cardholder)).thenReturn(cardholderDTO);

        var result = cardholderService.getAllActiveCardholders();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        verify(cardholderRepository).findByActiveTrue();
    }

    @Test
    void shouldCreateCardholder_whenCreateCardholderIsCalled() {
        when(cardholderMapper.toEntity(cardholderDTO)).thenReturn(cardholder);
        when(cardholderRepository.save(any(Cardholder.class))).thenReturn(cardholder);
        when(cardholderMapper.toDTO(cardholder)).thenReturn(cardholderDTO);

        CardholderDTO result = cardholderService.createCardholder(cardholderDTO);

        assertNotNull(result);
        assertEquals("john.doe@example.com", result.getEmail());
        verify(cardholderRepository).save(any(Cardholder.class));
    }

    @Test
    void shouldUpdateCardholder_whenUpdateCardholderIsCalled() {
        when(cardholderRepository.findById(1L)).thenReturn(Optional.of(cardholder));
        when(cardholderRepository.save(any(Cardholder.class))).thenReturn(cardholder);
        when(cardholderMapper.toDTO(cardholder)).thenReturn(cardholderDTO);

        CardholderDTO result = cardholderService.updateCardholder(1L, cardholderDTO);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(cardholderRepository).save(any(Cardholder.class));
    }
}