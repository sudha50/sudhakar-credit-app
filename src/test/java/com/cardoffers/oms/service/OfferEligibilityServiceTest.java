package com.cardoffers.oms.service;

import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
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
    private OfferRepository offerRepository; // Assuming a repository for fetching offers

    private static EligibleOfferResponseDTO anEligibleOfferResponseDTO() {
        EligibleOfferResponseDTO entity = new EligibleOfferResponseDTO();
        return entity;
    }

    private static OfferDTO anOfferDTO() {
        OfferDTO entity = new OfferDTO();
        entity.setTitle("Test Title");
        entity.setDescription("Test description");
        entity.setOfferType("DEFAULT");
        entity.setDiscountPercentage(new BigDecimal("1"));
        entity.setCashbackAmount(new BigDecimal("100.00"));
        entity.setMinimumPurchaseAmount(new BigDecimal("100.00"));
        entity.setStartDate(LocalDate.of(2025, 1, 15));
        entity.setEndDate(LocalDate.of(2025, 1, 15));
        entity.setTermsAndConditions("test-value");
        entity.setMerchant(null); // TODO: set MerchantDTO
        entity.setCardNetwork(null); // TODO: set CardNetworkDTO
        entity.setSource("test-value");
        entity.setMaxRedemptions(1);
        entity.setCurrentRedemptions(1);
        entity.setActive(true);
        return entity;
    }

    @Test
    void shouldReturnEligibleOffers_whenValidCardholderIdGiven() {
        // Arrange
        Long cardholderId = 1L;
        OfferDTO offer1 = anOfferDTO();
        OfferDTO offer2 = anOfferDTO();
        List<OfferDTO> offers = Arrays.asList(offer1, offer2);
        EligibleOfferResponseDTO expectedResponse = anEligibleOfferResponseDTO();
        expectedResponse.setEligibleOffers(offers);
        expectedResponse.setTotalOffers(2);
        expectedResponse.setCardholderId(cardholderId);

        when(offerRepository.findEligibleOffersByCardholderId(cardholderId)).thenReturn(offers);

        // Act
        EligibleOfferResponseDTO actualResponse = offerEligibilityService.getEligibleOffersForCardholder(cardholderId);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getTotalOffers(), actualResponse.getTotalOffers());
        assertEquals(expectedResponse.getEligibleOffers().size(), actualResponse.getEligibleOffers().size());
    }

    @Test
    void shouldReturnEmptyList_whenNoOffersAvailable() {
        // Arrange
        Long cardholderId = 1L;

        when(offerRepository.findEligibleOffersByCardholderId(cardholderId)).thenReturn(Collections.emptyList());

        // Act
        EligibleOfferResponseDTO actualResponse = offerEligibilityService.getEligibleOffersForCardholder(cardholderId);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(0, actualResponse.getTotalOffers());
        assertTrue(actualResponse.getEligibleOffers().isEmpty());
    }

    @Test
    void shouldFilterOffersByCategory_whenValidCategoryGiven() {
        // Arrange
        Long cardholderId = 1L;
        String category = "Electronics";
        OfferDTO offer = anOfferDTO();
        List<OfferDTO> offers = Collections.singletonList(offer);

        when(offerRepository.filterOffersByCategory(cardholderId, category)).thenReturn(offers);

        // Act
        List<OfferDTO> actualOffers = offerEligibilityService.filterOffersByCategory(cardholderId, category);

        // Assert
        assertNotNull(actualOffers);
        assertEquals(1, actualOffers.size());
    }

    @Test
    void shouldReturnEmptyList_whenFilteringByCategoryWithoutOffers() {
        // Arrange
        Long cardholderId = 1L;
        String category = "Non-existing Category";

        when(offerRepository.filterOffersByCategory(cardholderId, category)).thenReturn(Collections.emptyList());

        // Act
        List<OfferDTO> actualOffers = offerEligibilityService.filterOffersByCategory(cardholderId, category);

        // Assert
        assertNotNull(actualOffers);
        assertTrue(actualOffers.isEmpty());
    }

    @Test
    void shouldFilterOffersByOfferType_whenValidOfferTypeGiven() {
        // Arrange
        Long cardholderId = 1L;
        String offerType = "Promo";
        OfferDTO offer = anOfferDTO();
        List<OfferDTO> offers = Collections.singletonList(offer);

        when(offerRepository.filterOffersByOfferType(cardholderId, offerType)).thenReturn(offers);

        // Act
        List<OfferDTO> actualOffers = offerEligibilityService.filterOffersByOfferType(cardholderId, offerType);

        // Assert
        assertNotNull(actualOffers);
        assertEquals(1, actualOffers.size());
    }

    @Test
    void shouldReturnEmptyList_whenFilteringByOfferTypeWithoutOffers() {
        // Arrange
        Long cardholderId = 1L;
        String offerType = "Non-existing Type";

        when(offerRepository.filterOffersByOfferType(cardholderId, offerType)).thenReturn(Collections.emptyList());

        // Act
        List<OfferDTO> actualOffers = offerEligibilityService.filterOffersByOfferType(cardholderId, offerType);

        // Assert
        assertNotNull(actualOffers);
        assertTrue(actualOffers.isEmpty());
    }
}