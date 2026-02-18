package com.cardoffers.oms.model.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class EligibleOfferResponseDTOIntegrationTest {

    @Test
    public void testEligibleOfferResponseDTOCreation() {
        EligibleOfferResponseDTO responseDTO = new EligibleOfferResponseDTO();
        responseDTO.setCardholderId(1L);
        responseDTO.setCardholderName("John Doe");
        responseDTO.setTotalOffers(5);

        assertEquals(1L, responseDTO.getCardholderId());
        assertEquals("John Doe", responseDTO.getCardholderName());
        assertEquals(5, responseDTO.getTotalOffers());
        assertNotNull(responseDTO.getEligibleOffers());
        assertTrue(responseDTO.getEligibleOffers().isEmpty());
    }

    @Test
    public void testEligibleOfferResponseDTOWithOffers() {
        OfferDTO offer = new OfferDTO();
        offer.setId(1L);
        offer.setDescription("25% off on groceries");
        
        EligibleOfferResponseDTO responseDTO = new EligibleOfferResponseDTO();
        responseDTO.setCardholderId(1L);
        responseDTO.setCardholderName("Jane Doe");
        responseDTO.setTotalOffers(3);
        responseDTO.getEligibleOffers().add(offer);

        assertEquals(1L, responseDTO.getCardholderId());
        assertEquals("Jane Doe", responseDTO.getCardholderName());
        assertEquals(3, responseDTO.getTotalOffers());
        assertFalse(responseDTO.getEligibleOffers().isEmpty());
        assertEquals(1L, responseDTO.getEligibleOffers().get(0).getId());
    }
}