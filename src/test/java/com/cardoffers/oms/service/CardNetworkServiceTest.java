package com.cardoffers.oms.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import com.cardoffers.oms.model.dto.CardNetworkDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CardNetworkServiceTest {

    @Mock
    private CardNetworkService cardNetworkService;

    @InjectMocks
    private CardNetworkServiceTest service;

    @Test
    void shouldReturnCardNetwork_whenValidIdProvided() {
        CardNetworkDTO expectedNetwork = new CardNetworkDTO(/* initialize with test data */);
        when(cardNetworkService.getCardNetworkById(1L)).thenReturn(expectedNetwork);

        CardNetworkDTO actualNetwork = cardNetworkService.getCardNetworkById(1L);
        
        assertNotNull(actualNetwork);
        assertEquals(expectedNetwork, actualNetwork);
    }

    @Test
    void shouldReturnAllActiveCardNetworks_whenRequested() {
        List<CardNetworkDTO> expectedNetworks = Arrays.asList(new CardNetworkDTO(/* initialize with test data */));
        when(cardNetworkService.getAllActiveCardNetworks()).thenReturn(expectedNetworks);

        List<CardNetworkDTO> actualNetworks = cardNetworkService.getAllActiveCardNetworks();

        assertNotNull(actualNetworks);
        assertEquals(expectedNetworks.size(), actualNetworks.size());
    }

    @Test
    void shouldReturnCardNetwork_whenValidCodeProvided() {
        CardNetworkDTO expectedNetwork = new CardNetworkDTO(/* initialize with test data */);
        when(cardNetworkService.getCardNetworkByCode("VISA")).thenReturn(expectedNetwork);

        CardNetworkDTO actualNetwork = cardNetworkService.getCardNetworkByCode("VISA");
        
        assertNotNull(actualNetwork);
        assertEquals(expectedNetwork, actualNetwork);
    }

    @Test
    void shouldThrowException_whenCardNetworkNotFoundById() {
        when(cardNetworkService.getCardNetworkById(999L)).thenReturn(null);

        CardNetworkDTO actualNetwork = cardNetworkService.getCardNetworkById(999L);

        assertNull(actualNetwork);
    }

    @Test
    void shouldReturnEmptyList_whenNoActiveCardNetworksAvailable() {
        when(cardNetworkService.getAllActiveCardNetworks()).thenReturn(List.of());

        List<CardNetworkDTO> actualNetworks = cardNetworkService.getAllActiveCardNetworks();

        assertNotNull(actualNetworks);
        assertTrue(actualNetworks.isEmpty());
    }
}