package com.cardoffers.oms.service;

import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cardoffers.oms.model.dto.EligibleOfferResponseDTO;
import com.cardoffers.oms.model.dto.OfferDTO;

@ExtendWith(MockitoExtension.class)
class OfferEligibilityServiceTest {

    @InjectMocks
    OfferEligibilityService offerEligibilityService;

    @Mock
    private OfferRepository offerRepository;  // Assuming there's an OfferRepository interface for database operations

    private static EligibleOfferResponseDTO anEligibleOfferResponseDTO() {
        EligibleOfferResponseDTO entity = new EligibleOfferResponseDTO();
        return entity;
    }

    private static OfferDTO anOfferDTO() {
        OfferDTO entity = new OfferDTO();
        entity.setTitle("Test Title");
        entity.setDescription("Test description");
        entity.setOfferType("DEFAULT");
        entity.setDiscountPercentage(1);
        entity.setCashbackAmount(new java.math.BigDecimal("100.00"));
        entity.setMinimumPurchaseAmount(new java.math.BigDecimal("100.00"));
        entity.setStartDate(java.time.LocalDate.of(2025, 1, 15));
        entity.setEndDate(java.time.LocalDate.of(2025, 1, 15));
        entity.setTermsAndConditions("test-value");
        entity.setMerchant(null);  // Set MerchantDTO as needed
        entity.setCardNetwork(null);  // Set CardNetworkDTO as needed
        entity.setSource("test-value");
        entity.setMaxRedemptions(1);
        entity.setCurrentRedemptions(1);
        entity.setActive(true);
        return entity;
    }

    @Test
    void shouldReturnEligibleOffers_whenCardholderExists() {
        Long cardholderId = 1L;
        OfferDTO offer = anOfferDTO();
        EligibleOfferResponseDTO expectedResponse = anEligibleOfferResponseDTO();
        expectedResponse.setCardholderId(cardholderId);
        expectedResponse.setEligibleOffers(Collections.singletonList(offer));
        expectedResponse.setTotalOffers(1);

        when(offerRepository.findEligibleOffersByCardholderId(cardholderId)).thenReturn(expectedResponse);

        EligibleOfferResponseDTO actualResponse = offerEligibilityService.getEligibleOffersForCardholder(cardholderId);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getTotalOffers(), actualResponse.getTotalOffers());
        assertEquals(expectedResponse.getEligibleOffers().size(), actualResponse.getEligibleOffers().size());
    }

    @Test
    void shouldReturnEmptyList_whenNoOffersFoundByCategory() {
        Long cardholderId = 1L;
        String category = "Non-existent Category";

        when(offerRepository.findOffersByCategory(cardholderId, category)).thenReturn(Collections.emptyList());

        List<OfferDTO> offers = offerEligibilityService.filterOffersByCategory(cardholderId, category);

        assertNotNull(offers);
        assertTrue(offers.isEmpty());
    }

    @Test
    void shouldReturnFilteredOffers_whenOffersMatchOfferType() {
        Long cardholderId = 1L;
        String offerType = "Discount";

        OfferDTO offer = anOfferDTO();
        offer.setOfferType(offerType);
        when(offerRepository.findOffersByOfferType(cardholderId, offerType)).thenReturn(Collections.singletonList(offer));

        List<OfferDTO> offers = offerEligibilityService.filterOffersByOfferType(cardholderId, offerType);

        assertNotNull(offers);
        assertEquals(1, offers.size());
        assertEquals(offerType, offers.get(0).getOfferType());
    }

    @Test
    void shouldHandleNullCardholderId_whenGettingEligibleOffers() {
        Long cardholderId = null;

        assertThrows(IllegalArgumentException.class, () -> {
            offerEligibilityService.getEligibleOffersForCardholder(cardholderId);
        });
    }

    @Test
    void shouldReturnList_whenOffersExistByCategory() {
        Long cardholderId = 1L;
        String category = "Special Offers";
        OfferDTO offer = anOfferDTO();
        when(offerRepository.findOffersByCategory(cardholderId, category)).thenReturn(Arrays.asList(offer));

        List<OfferDTO> offers = offerEligibilityService.filterOffersByCategory(cardholderId, category);

        assertNotNull(offers);
        assertFalse(offers.isEmpty());
        assertEquals(1, offers.size());
    }
}