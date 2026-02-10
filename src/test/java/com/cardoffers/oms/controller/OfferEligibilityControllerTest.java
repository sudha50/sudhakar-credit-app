package com.cardoffers.oms.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import com.cardoffers.oms.model.dto.EligibleOfferResponseDTO;
import com.cardoffers.oms.model.dto.OfferDTO;
import com.cardoffers.oms.service.OfferEligibilityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(OfferEligibilityController.class)
class OfferEligibilityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private OfferEligibilityService offerEligibilityService;

    @InjectMocks
    private OfferEligibilityController offerEligibilityController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(offerEligibilityController).build();
    }

    @Test
    void shouldReturnEligibleOffers_whenValidCardholderId() throws Exception {
        Long cardholderId = 1L;
        EligibleOfferResponseDTO expectedResponse = new EligibleOfferResponseDTO();

        when(offerEligibilityService.getEligibleOffersForCardholder(cardholderId)).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/v1/eligible-offers/cardholder/{cardholderId}", cardholderId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.offers").exists());

        verify(offerEligibilityService).getEligibleOffersForCardholder(cardholderId);
    }

    @Test
    void shouldReturnOffersByCategory_whenValidRequest() throws Exception {
        Long cardholderId = 2L;
        String category = "travel";
        List<OfferDTO> expectedOffers = List.of(new OfferDTO());

        when(offerEligibilityService.filterOffersByCategory(cardholderId, category)).thenReturn(expectedOffers);

        mockMvc.perform(get("/api/v1/eligible-offers/cardholder/{cardholderId}/category/{category}", 
                cardholderId, category).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").exists());

        verify(offerEligibilityService).filterOffersByCategory(cardholderId, category);
    }

    @Test
    void shouldReturnOffersByType_whenValidRequest() throws Exception {
        Long cardholderId = 3L;
        String offerType = "cashback";
        List<OfferDTO> expectedOffers = List.of(new OfferDTO());

        when(offerEligibilityService.filterOffersByOfferType(cardholderId, offerType)).thenReturn(expectedOffers);

        mockMvc.perform(get("/api/v1/eligible-offers/cardholder/{cardholderId}/type/{offerType}", 
                cardholderId, offerType).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").exists());
        
        verify(offerEligibilityService).filterOffersByOfferType(cardholderId, offerType);
    }

    @Test
    void shouldReturnNotFound_whenCardholderIdNotFound() throws Exception {
        Long cardholderId = 999L;

        when(offerEligibilityService.getEligibleOffersForCardholder(cardholderId)).thenThrow(new RuntimeException("Not Found"));

        mockMvc.perform(get("/api/v1/eligible-offers/cardholder/{cardholderId}", cardholderId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(offerEligibilityService).getEligibleOffersForCardholder(cardholderId);
    }
    
    @Test
    void shouldReturnBadRequest_whenInvalidCardholderId() throws Exception {
        mockMvc.perform(get("/api/v1/eligible-offers/cardholder/{cardholderId}", "invalid")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}