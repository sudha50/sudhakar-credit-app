package com.cardoffers.oms.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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
import com.cardoffers.oms.mapper.CardNetworkMapper;
import com.cardoffers.oms.model.dto.CardNetworkDTO;
import com.cardoffers.oms.model.entity.CardNetwork;
import com.cardoffers.oms.repository.CardNetworkRepository;

@ExtendWith(MockitoExtension.class)
class CardNetworkServiceImplTest {

    @Mock
    private CardNetworkRepository cardNetworkRepository;

    @Mock
    private CardNetworkMapper cardNetworkMapper;

    @InjectMocks
    private CardNetworkServiceImpl cardNetworkService;

    private CardNetwork cardNetwork;
    private CardNetworkDTO cardNetworkDTO;

    @BeforeEach
    void setUp() {
        cardNetwork = new CardNetwork();
        cardNetwork.setId(1L);
        cardNetwork.setCode("VISA");

        cardNetworkDTO = new CardNetworkDTO();
        cardNetworkDTO.setId(1L);
        cardNetworkDTO.setCode("VISA");
    }

    @Test
    void shouldReturnCardNetworkDTO_whenCardNetworkExists() {
        when(cardNetworkRepository.findById(anyLong())).thenReturn(Optional.of(cardNetwork));
        when(cardNetworkMapper.toDTO(cardNetwork)).thenReturn(cardNetworkDTO);

        CardNetworkDTO result = cardNetworkService.getCardNetworkById(1L);

        assertNotNull(result);
        assertEquals(cardNetworkDTO.getCode(), result.getCode());
    }

    @Test
    void shouldThrowResourceNotFoundException_whenCardNetworkDoesNotExist() {
        when(cardNetworkRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cardNetworkService.getCardNetworkById(1L));
    }

    @Test
    void shouldReturnListOfActiveCardNetworks_whenActiveNetworksExist() {
        when(cardNetworkRepository.findByActiveTrue()).thenReturn(Collections.singletonList(cardNetwork));
        when(cardNetworkMapper.toDTO(cardNetwork)).thenReturn(cardNetworkDTO);

        var result = cardNetworkService.getAllActiveCardNetworks();

        assertEquals(1, result.size());
        assertEquals(cardNetworkDTO.getCode(), result.get(0).getCode());
    }

    @Test
    void shouldReturnEmptyList_whenNoActiveCardNetworksExist() {
        when(cardNetworkRepository.findByActiveTrue()).thenReturn(Collections.emptyList());

        var result = cardNetworkService.getAllActiveCardNetworks();

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnCardNetworkDTO_whenCardNetworkFoundByCode() {
        when(cardNetworkRepository.findByCode(anyString())).thenReturn(Optional.of(cardNetwork));
        when(cardNetworkMapper.toDTO(cardNetwork)).thenReturn(cardNetworkDTO);

        CardNetworkDTO result = cardNetworkService.getCardNetworkByCode("VISA");

        assertNotNull(result);
        assertEquals(cardNetworkDTO.getCode(), result.getCode());
    }

    @Test
    void shouldThrowResourceNotFoundException_whenCardNetworkNotFoundByCode() {
        when(cardNetworkRepository.findByCode(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cardNetworkService.getCardNetworkByCode("UNKNOWN"));
    }
}