package com.cardoffers.oms.controller;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import com.cardoffers.oms.model.dto.CardNetworkDTO;
import com.cardoffers.oms.service.CardNetworkService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardNetworkControllerTest {

    @Mock
    private CardNetworkService cardNetworkService;

    @InjectMocks
    private CardNetworkController cardNetworkController;

    @Test
    void shouldReturnAllCardNetworks_whenGetAllCardNetworksIsCalled() {
        CardNetworkDTO dto = new CardNetworkDTO(); // Assuming a default constructor is available
        when(cardNetworkService.getAllActiveCardNetworks()).thenReturn(Collections.singletonList(dto));
        
        ResponseEntity<List<CardNetworkDTO>> response = cardNetworkController.getAllCardNetworks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldReturnCardNetworkById_whenGetCardNetworkByIdIsCalled() {
        Long id = 1L;
        CardNetworkDTO dto = new CardNetworkDTO(); // Assuming a default constructor is available
        when(cardNetworkService.getCardNetworkById(id)).thenReturn(dto);
        
        ResponseEntity<CardNetworkDTO> response = cardNetworkController.getCardNetworkById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void shouldReturnCardNetworkByCode_whenGetCardNetworkByCodeIsCalled() {
        String code = "visa";
        CardNetworkDTO dto = new CardNetworkDTO(); // Assuming a default constructor is available
        when(cardNetworkService.getCardNetworkByCode(code)).thenReturn(dto);
        
        ResponseEntity<CardNetworkDTO> response = cardNetworkController.getCardNetworkByCode(code);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void shouldReturn404_whenCardNetworkByIdNotFound() {
        Long id = 2L;
        when(cardNetworkService.getCardNetworkById(id)).thenReturn(null);
        
        ResponseEntity<CardNetworkDTO> response = cardNetworkController.getCardNetworkById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldReturn404_whenCardNetworkByCodeNotFound() {
        String code = "unknown";
        when(cardNetworkService.getCardNetworkByCode(code)).thenReturn(null);
        
        ResponseEntity<CardNetworkDTO> response = cardNetworkController.getCardNetworkByCode(code);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}