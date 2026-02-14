package com.cardoffers.oms.service;

import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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

@ExtendWith(MockitoExtension.class)
class OfferServiceTest {

    @InjectMocks 
    OfferService offerService;

    @Mock
    private OfferRepository offerRepository; // Assuming an OfferRepository exists

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
        entity.setMerchant(null); // Set MerchantDTO accordingly
        entity.setCardNetwork(null); // Set CardNetworkDTO accordingly
        entity.setSource("test-value");
        entity.setMaxRedemptions(1);
        entity.setCurrentRedemptions(1);
        entity.setActive(true);
        return entity;
    }

    private static OfferSummaryDTO anOfferSummaryDTO() {
        OfferSummaryDTO entity = new OfferSummaryDTO();
        entity.setTitle("Test Summary Title");
        entity.setMerchantName("Test Merchant");
        entity.setOfferType("DEFAULT");
        entity.setDiscountPercentage(BigDecimal.ONE);
        entity.setStartDate(LocalDate.of(2025, 1, 15));
        entity.setEndDate(LocalDate.of(2025, 1, 15));
        entity.setActive(true);
        return entity;
    }

    @Test
    void shouldReturnOffer_whenGetOfferByIdIsCalled() {
        OfferDTO expectedOffer = anOfferDTO();
        when(offerRepository.findById(anyLong())).thenReturn(Optional.of(expectedOffer));

        OfferDTO actualOffer = offerService.getOfferById(1L);

        assertNotNull(actualOffer);
        assertEquals(expectedOffer.getTitle(), actualOffer.getTitle());
        verify(offerRepository).findById(1L);
    }

    @Test
    void shouldReturnEmptyList_whenNoOffersByMerchant() {
        when(offerRepository.findByMerchantId(anyLong())).thenReturn(Collections.emptyList());

        List<OfferSummaryDTO> offers = offerService.getOffersByMerchant(1L);

        assertNotNull(offers);
        assertTrue(offers.isEmpty());
        verify(offerRepository).findByMerchantId(1L);
    }

    @Test
    void shouldReturnListOfOffersByCardNetwork_whenCalled() {
        OfferSummaryDTO expectedSummary = anOfferSummaryDTO();
        when(offerRepository.findByCardNetworkId(anyLong())).thenReturn(List.of(expectedSummary));

        List<OfferSummaryDTO> summaries = offerService.getOffersByCardNetwork(1L);

        assertNotNull(summaries);
        assertFalse(summaries.isEmpty());
        assertEquals(expectedSummary.getTitle(), summaries.get(0).getTitle());
        verify(offerRepository).findByCardNetworkId(1L);
    }

    @Test
    void shouldCreateOffer_whenValidDtoIsProvided() {
        OfferDTO newOffer = anOfferDTO();
        when(offerRepository.save(any(OfferDTO.class))).thenReturn(newOffer);

        OfferDTO createdOffer = offerService.createOffer(newOffer);

        assertNotNull(createdOffer);
        assertEquals(newOffer.getTitle(), createdOffer.getTitle());
        verify(offerRepository).save(newOffer);
    }

    @Test
    void shouldUpdateOffer_whenValidIdAndDtoAreProvided() {
        OfferDTO existingOffer = anOfferDTO();
        existingOffer.setId(1L);
        when(offerRepository.findById(anyLong())).thenReturn(Optional.of(existingOffer));
        when(offerRepository.save(any(OfferDTO.class))).thenReturn(existingOffer);

        OfferDTO updatedOffer = offerService.updateOffer(1L, existingOffer);

        assertNotNull(updatedOffer);
        assertEquals(existingOffer.getTitle(), updatedOffer.getTitle());
        verify(offerRepository).findById(1L);
        verify(offerRepository).save(existingOffer);
    }

    @Test
    void shouldSearchOffers_whenKeywordIsProvided() {
        OfferDTO expectedOffer = anOfferDTO();
        when(offerRepository.findByKeyword(anyString())).thenReturn(List.of(expectedOffer));

        List<OfferDTO> results = offerService.searchOffers("Test", null, null);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(expectedOffer.getTitle(), results.get(0).getTitle());
        verify(offerRepository).findByKeyword("Test");
    }
}