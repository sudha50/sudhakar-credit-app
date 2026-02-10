package com.cardoffers.oms.controller;

import java.util.Collections;
import java.util.List;

import com.cardoffers.oms.model.dto.OfferDTO;
import com.cardoffers.oms.model.dto.OfferSummaryDTO;
import com.cardoffers.oms.service.OfferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfferControllerTest {

    @Mock
    private OfferService offerService;

    @InjectMocks
    private OfferController offerController;

    private OfferDTO offerDTO;

    @BeforeEach
    void setUp() {
        offerDTO = new OfferDTO();
        // Initialize offerDTO as necessary
    }

    @Test
    void shouldReturnAllActiveOffers_whenGetAllActiveOffersIsCalled() {
        List<OfferDTO> offers = Collections.singletonList(offerDTO);
        when(offerService.getAllActiveOffers()).thenReturn(offers);
        
        ResponseEntity<List<OfferDTO>> response = offerController.getAllActiveOffers();
        
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(offers, response.getBody());
    }

    @Test
    void shouldReturnOffer_whenGetOfferByIdIsCalled() {
        Long offerId = 1L;
        when(offerService.getOfferById(offerId)).thenReturn(offerDTO);
        
        ResponseEntity<OfferDTO> response = offerController.getOfferById(offerId);
        
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(offerDTO, response.getBody());
    }

    @Test
    void shouldReturnOffersByMerchant_whenGetOffersByMerchantIsCalled() {
        Long merchantId = 1L;
        List<OfferSummaryDTO> offerSummaries = Collections.singletonList(new OfferSummaryDTO());
        when(offerService.getOffersByMerchant(merchantId)).thenReturn(offerSummaries);
        
        ResponseEntity<List<OfferSummaryDTO>> response = offerController.getOffersByMerchant(merchantId);
        
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(offerSummaries, response.getBody());
    }

    @Test
    void shouldCreateOffer_whenCreateOfferIsCalled() {
        when(offerService.createOffer(any(OfferDTO.class))).thenReturn(offerDTO);
        
        ResponseEntity<OfferDTO> response = offerController.createOffer(offerDTO);
        
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(offerDTO, response.getBody());
    }

    @Test
    void shouldUpdateOffer_whenUpdateOfferIsCalled() {
        Long offerId = 1L;
        when(offerService.updateOffer(offerId, offerDTO)).thenReturn(offerDTO);
        
        ResponseEntity<OfferDTO> response = offerController.updateOffer(offerId, offerDTO);
        
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(offerDTO, response.getBody());
    }

    @Test
    void shouldReturnOffersByNetwork_whenGetOffersByNetworkIsCalled() {
        Long networkId = 1L;
        List<OfferSummaryDTO> offerSummaries = Collections.singletonList(new OfferSummaryDTO());
        when(offerService.getOffersByCardNetwork(networkId)).thenReturn(offerSummaries);
        
        ResponseEntity<List<OfferSummaryDTO>> response = offerController.getOffersByNetwork(networkId);
        
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(offerSummaries, response.getBody());
    }

    @Test
    void shouldSearchOffers_whenSearchOffersIsCalled() {
        String keyword = "discount";
        List<OfferDTO> offers = Collections.singletonList(offerDTO);
        when(offerService.searchOffers(keyword, null, null)).thenReturn(offers);
        
        ResponseEntity<List<OfferDTO>> response = offerController.searchOffers(keyword, null, null);
        
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(offers, response.getBody());
    }
}