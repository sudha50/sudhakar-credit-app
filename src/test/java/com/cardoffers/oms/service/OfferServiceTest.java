package com.cardoffers.oms.service;

import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cardoffers.oms.model.dto.OfferDTO;
import com.cardoffers.oms.model.dto.OfferSummaryDTO;

@ExtendWith(MockitoExtension.class)
class OfferServiceTest {

    @InjectMocks
    OfferService offerService;

    @Mock
    OfferRepository offerRepository; // Assuming there's a repository to mock, adjust according to actual dependencies.

    private static OfferDTO anOfferDTO() {
        OfferDTO entity = new OfferDTO();
        entity.setTitle("Test Title");
        entity.setDescription("Test description");
        entity.setOfferType("DEFAULT");
        entity.setDiscountPercentage(BigDecimal.ONE);
        entity.setCashbackAmount(new BigDecimal("100.00"));
        entity.setMinimumPurchaseAmount(new BigDecimal("100.00"));
        entity.setStartDate(LocalDate.of(2025, 1, 15));
        entity.setEndDate(LocalDate.of(2025, 1, 15));
        entity.setTermsAndConditions("test-value");
        entity.setMerchant(null /* TODO: set MerchantDTO */);
        entity.setCardNetwork(null /* TODO: set CardNetworkDTO */);
        entity.setSource("test-value");
        entity.setMaxRedemptions(1);
        entity.setCurrentRedemptions(1);
        entity.setActive(true);
        return entity;
    }

    private static OfferSummaryDTO anOfferSummaryDTO() {
        OfferSummaryDTO entity = new OfferSummaryDTO();
        return entity;
    }

    @Test
    void shouldReturnOffer_whenValidIdProvided() {
        OfferDTO expectedOffer = anOfferDTO();
        expectedOffer.setId(1L); // assuming it should have an ID
        when(offerRepository.findById(1L)).thenReturn(Optional.of(expectedOffer));

        OfferDTO actualOffer = offerService.getOfferById(1L);

        assertNotNull(actualOffer);
        assertEquals(expectedOffer.getTitle(), actualOffer.getTitle());
        verify(offerRepository).findById(1L);
    }

    @Test
    void shouldReturnOffersByMerchant_whenValidMerchantIdProvided() {
        Long merchantId = 1L;
        OfferSummaryDTO offerSummary = anOfferSummaryDTO();
        List<OfferSummaryDTO> expectedOffers = Collections.singletonList(offerSummary);

        when(offerRepository.findOffersByMerchantId(merchantId)).thenReturn(expectedOffers);

        List<OfferSummaryDTO> actualOffers = offerService.getOffersByMerchant(merchantId);

        assertNotNull(actualOffers);
        assertEquals(expectedOffers.size(), actualOffers.size());
        verify(offerRepository).findOffersByMerchantId(merchantId);
    }

    @Test
    void shouldReturnOffersByCardNetwork_whenValidNetworkIdProvided() {
        Long networkId = 1L;
        OfferSummaryDTO offerSummary = anOfferSummaryDTO();
        List<OfferSummaryDTO> expectedOffers = Collections.singletonList(offerSummary);

        when(offerRepository.findOffersByCardNetworkId(networkId)).thenReturn(expectedOffers);

        List<OfferSummaryDTO> actualOffers = offerService.getOffersByCardNetwork(networkId);

        assertNotNull(actualOffers);
        assertEquals(expectedOffers.size(), actualOffers.size());
        verify(offerRepository).findOffersByCardNetworkId(networkId);
    }

    @Test
    void shouldCreateOffer_whenValidOfferProvided() {
        OfferDTO offerToCreate = anOfferDTO();
        offerToCreate.setId(null); // Ensure ID is not set prior to creation
        
        when(offerRepository.save(any())).thenReturn(offerToCreate);

        OfferDTO createdOffer = offerService.createOffer(offerToCreate);

        assertNotNull(createdOffer);
        assertEquals(offerToCreate.getTitle(), createdOffer.getTitle());
        verify(offerRepository).save(offerToCreate);
    }

    @Test
    void shouldUpdateOffer_whenValidIdAndOfferProvided() {
        Long offerId = 1L;
        OfferDTO existingOffer = anOfferDTO();
        existingOffer.setId(offerId);
        OfferDTO updatedOffer = anOfferDTO();
        updatedOffer.setId(offerId);

        when(offerRepository.findById(offerId)).thenReturn(Optional.of(existingOffer));
        when(offerRepository.save(any())).thenReturn(updatedOffer);

        OfferDTO result = offerService.updateOffer(offerId, updatedOffer);

        assertNotNull(result);
        assertEquals(updatedOffer.getTitle(), result.getTitle());
        verify(offerRepository).findById(offerId);
        verify(offerRepository).save(updatedOffer);
    }

    @Test
    void shouldReturnOffers_whenSearchCriteriaProvided() {
        String keyword = "Test";
        String offerType = "DEFAULT";
        String category = "Food";
        OfferDTO offer = anOfferDTO();
        List<OfferDTO> expectedOffers = Collections.singletonList(offer);
        
        when(offerRepository.searchOffers(any(), any(), any())).thenReturn(expectedOffers);

        List<OfferDTO> actualOffers = offerService.searchOffers(keyword, offerType, category);

        assertNotNull(actualOffers);
        assertEquals(expectedOffers.size(), actualOffers.size());
        verify(offerRepository).searchOffers(keyword, offerType, category);
    }
}