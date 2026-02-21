package com.cardoffers.oms.model.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class EligibleOfferResponseDTOTest {

    @Test
    void shouldCreateEligibleOfferResponseDTO_whenAllFieldsSet() {
        EligibleOfferResponseDTO responseDTO = new EligibleOfferResponseDTO();
        responseDTO.setCardholderId(1L);
        responseDTO.setCardholderName("John Doe");
        responseDTO.setEligibleOffers(Collections.emptyList());
        responseDTO.setTotalOffers(0);

        assertNotNull(responseDTO);
        assertEquals(1L, responseDTO.getCardholderId());
        assertEquals("John Doe", responseDTO.getCardholderName());
        assertEquals(0, responseDTO.getTotalOffers());
        assertTrue(responseDTO.getEligibleOffers().isEmpty());
    }

    @Test
    void shouldSetEligibleOffers_whenListProvided() {
        EligibleOfferResponseDTO responseDTO = new EligibleOfferResponseDTO();
        OfferDTO offer = new OfferDTO(); // Assuming OfferDTO has a no-args constructor
        // Populate OfferDTO as needed
        responseDTO.setEligibleOffers(Collections.singletonList(offer));

        assertNotNull(responseDTO.getEligibleOffers());
        assertEquals(1, responseDTO.getEligibleOffers().size());
    }

    @Test
    void shouldReturnDefaultEmptyList_whenNoEligibleOffersSet() {
        EligibleOfferResponseDTO responseDTO = new EligibleOfferResponseDTO();
        
        assertNotNull(responseDTO.getEligibleOffers());
        assertTrue(responseDTO.getEligibleOffers().isEmpty());
    }

    @Test
    void shouldCreateEligibleOfferResponseDTO_withBuilder() {
        EligibleOfferResponseDTO responseDTO = EligibleOfferResponseDTO.builder()
                .cardholderId(2L)
                .cardholderName("Jane Doe")
                .totalOffers(5)
                .build();

        assertNotNull(responseDTO);
        assertEquals(2L, responseDTO.getCardholderId());
        assertEquals("Jane Doe", responseDTO.getCardholderName());
        assertEquals(5, responseDTO.getTotalOffers());
        assertTrue(responseDTO.getEligibleOffers().isEmpty());
    }

    @Test
    void shouldSetCardholderName_whenProvided() {
        EligibleOfferResponseDTO responseDTO = new EligibleOfferResponseDTO();
        responseDTO.setCardholderName("Alice Smith");
        
        assertEquals("Alice Smith", responseDTO.getCardholderName());
    }

    @Test
    void shouldSetTotalOffers_whenProvided() {
        EligibleOfferResponseDTO responseDTO = new EligibleOfferResponseDTO();
        responseDTO.setTotalOffers(10);
        
        assertEquals(10, responseDTO.getTotalOffers());
    }
}