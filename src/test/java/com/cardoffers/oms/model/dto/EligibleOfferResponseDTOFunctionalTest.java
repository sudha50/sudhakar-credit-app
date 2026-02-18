package com.cardoffers.oms.model.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;

@SpringBootTest
@ActiveProfiles("test")
class EligibleOfferResponseDTOFunctionalTest {

    @Test
    void shouldCreateEligibleOfferResponseDTO() {
        EligibleOfferResponseDTO responseDTO = new EligibleOfferResponseDTO();
        responseDTO.setCardholderId(1L);
        responseDTO.setCardholderName("John Doe");
        responseDTO.setEligibleOffers(Arrays.asList(new OfferDTO(), new OfferDTO()));
        responseDTO.setTotalOffers(2);

        assertEquals(1L, responseDTO.getCardholderId());
        assertEquals("John Doe", responseDTO.getCardholderName());
        assertEquals(2, responseDTO.getEligibleOffers().size());
        assertEquals(2, responseDTO.getTotalOffers());
    }

    @Test
    void shouldDefaultEligibleOffersToEmptyList() {
        EligibleOfferResponseDTO responseDTO = new EligibleOfferResponseDTO();
        responseDTO.setCardholderId(2L);
        responseDTO.setCardholderName("Jane Doe");

        assertNotNull(responseDTO.getEligibleOffers());
        assertTrue(responseDTO.getEligibleOffers().isEmpty());
    }

    @Test
    void shouldAllowUpdateEligibleOffers() {
        EligibleOfferResponseDTO responseDTO = new EligibleOfferResponseDTO();
        responseDTO.setEligibleOffers(Arrays.asList(new OfferDTO()));

        assertEquals(1, responseDTO.getEligibleOffers().size());

        responseDTO.setEligibleOffers(null);
        assertNull(responseDTO.getEligibleOffers());
    }
}