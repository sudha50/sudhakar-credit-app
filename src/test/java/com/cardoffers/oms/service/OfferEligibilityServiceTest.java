package com.cardoffers.oms.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cardoffers.oms.model.dto.EligibleOfferResponseDTO;
import com.cardoffers.oms.model.dto.OfferDTO;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class OfferEligibilityServiceTest {

    @Mock
    private OfferEligibilityService offerEligibilityService;

    @InjectMocks
    private OfferEligibilityServiceImpl offerEligibilityServiceImpl;

    @Test
    void shouldReturnEligibleOffers_whenCardholderExists() {
        Long cardholderId = 1L;
        EligibleOfferResponseDTO expectedResponse = new EligibleOfferResponseDTO();

        when(offerEligibilityService.getEligibleOffersForCardholder(cardholderId)).thenReturn(expectedResponse);

        EligibleOfferResponseDTO actualResponse = offerEligibilityServiceImpl.getEligibleOffersForCardholder(cardholderId);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void shouldReturnFilteredOffersByCategory_whenValidInputsProvided() {
        Long cardholderId = 1L;
        String category = "Electronics";
        OfferDTO offerDTO = new OfferDTO();
        List<OfferDTO> expectedOffers = List.of(offerDTO);

        when(offerEligibilityService.filterOffersByCategory(cardholderId, category)).thenReturn(expectedOffers);

        List<OfferDTO> actualOffers = offerEligibilityServiceImpl.filterOffersByCategory(cardholderId, category);

        assertNotNull(actualOffers);
        assertEquals(1, actualOffers.size());
        assertEquals(expectedOffers, actualOffers);
    }

    @Test
    void shouldReturnFilteredOffersByOfferType_whenValidInputsProvided() {
        Long cardholderId = 1L;
        String offerType = "Cashback";
        OfferDTO offerDTO = new OfferDTO();
        List<OfferDTO> expectedOffers = List.of(offerDTO);

        when(offerEligibilityService.filterOffersByOfferType(cardholderId, offerType)).thenReturn(expectedOffers);

        List<OfferDTO> actualOffers = offerEligibilityServiceImpl.filterOffersByOfferType(cardholderId, offerType);

        assertNotNull(actualOffers);
        assertEquals(1, actualOffers.size());
        assertEquals(expectedOffers, actualOffers);
    }

    @Test
    void shouldReturnEmptyList_whenNoOffersAvailableForCategory() {
        Long cardholderId = 1L;
        String category = "NonExistentCategory";

        when(offerEligibilityService.filterOffersByCategory(cardholderId, category)).thenReturn(Collections.emptyList());

        List<OfferDTO> actualOffers = offerEligibilityServiceImpl.filterOffersByCategory(cardholderId, category);

        assertNotNull(actualOffers);
        assertTrue(actualOffers.isEmpty());
    }

    @Test
    void shouldThrowException_whenCardholderNotFound() {
        Long cardholderId = 999L;

        when(offerEligibilityService.getEligibleOffersForCardholder(cardholderId)).thenThrow(new IllegalArgumentException("Cardholder not found"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            offerEligibilityServiceImpl.getEligibleOffersForCardholder(cardholderId)
        );

        assertEquals("Cardholder not found", exception.getMessage());
    }
}