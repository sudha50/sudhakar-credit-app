package com.cardoffers.oms.model.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.Collections;

public class EligibleOfferResponseDTOTest {

    @Test
    void shouldCreateEligibleOfferResponseDTO_whenAllFieldsAreProvided() {
        EligibleOfferResponseDTO responseDTO = new EligibleOfferResponseDTO();
        responseDTO.setCardholderId(1L);
        responseDTO.setCardholderName("John Doe");
        responseDTO.setEligibleOffers(Collections.emptyList());
        responseDTO.setTotalOffers(0);

        assertNotNull(responseDTO);
        assertEquals(1L, responseDTO.getCardholderId());
        assertEquals("John Doe", responseDTO.getCardholderName());
        assertEquals(0, responseDTO.getTotalOffers());
    }

    @Test
    void shouldInitializeEligibleOffers_whenNotProvided() {
        EligibleOfferResponseDTO responseDTO = new EligibleOfferResponseDTO();

        assertNotNull(responseDTO.getEligibleOffers());
        assertTrue(responseDTO.getEligibleOffers().isEmpty());
    }

    @Test
    void shouldSetEligibleOffers_whenProvided() {
        EligibleOfferResponseDTO responseDTO = new EligibleOfferResponseDTO();
        OfferDTO offer = new OfferDTO(); // Assuming OfferDTO has a no-arg constructor and necessary setters.
        offer.setId(1L); // Assuming ID is a required field.
        offer.setDescription("Special Offer");
        responseDTO.setEligibleOffers(Collections.singletonList(offer));

        assertEquals(1, responseDTO.getEligibleOffers().size());
        assertEquals("Special Offer", responseDTO.getEligibleOffers().get(0).getDescription());
    }

    @Test
    void shouldSetTotalOffers_whenProvided() {
        EligibleOfferResponseDTO responseDTO = new EligibleOfferResponseDTO();
        responseDTO.setTotalOffers(5);

        assertEquals(5, responseDTO.getTotalOffers());
    }

    @Test
    void shouldReturnDefaultEligibleOffers_whenNoOffersProvided() {
        EligibleOfferResponseDTO responseDTO = new EligibleOfferResponseDTO();
        
        assertEquals(0, responseDTO.getEligibleOffers().size());
    }

    @Test
    void shouldReturnCorrectCardholderName_whenSet() {
        EligibleOfferResponseDTO responseDTO = new EligibleOfferResponseDTO();
        responseDTO.setCardholderName("Jane Doe");
        
        assertEquals("Jane Doe", responseDTO.getCardholderName());
    }
}