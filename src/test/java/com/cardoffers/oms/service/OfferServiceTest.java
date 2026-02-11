package com.cardoffers.oms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cardoffers.oms.model.dto.OfferDTO;
import com.cardoffers.oms.model.dto.OfferSummaryDTO;

@ExtendWith(MockitoExtension.class)
class OfferServiceTest {

    @Mock
    private OfferService offerService;

    @InjectMocks
    private OfferServiceImpl offerServiceImpl; // Assuming there's an implementation class

    @Test
    void shouldReturnOffer_whenGetOfferByIdIsCalled() {
        Long offerId = 1L;
        OfferDTO offer = new OfferDTO();
        when(offerService.getOfferById(offerId)).thenReturn(offer);
        
        OfferDTO result = offerServiceImpl.getOfferById(offerId);
        
        assertNotNull(result);
        assertEquals(offer, result);
        verify(offerService).getOfferById(offerId);
    }

    @Test
    void shouldReturnActiveOffers_whenGetAllActiveOffersIsCalled() {
        List<OfferDTO> activeOffers = Arrays.asList(new OfferDTO(), new OfferDTO());
        when(offerService.getAllActiveOffers()).thenReturn(activeOffers);
        
        List<OfferDTO> result = offerServiceImpl.getAllActiveOffers();
        
        assertEquals(2, result.size());
        verify(offerService).getAllActiveOffers();
    }

    @Test
    void shouldReturnOffersByMerchant_whenGetOffersByMerchantIsCalled() {
        Long merchantId = 2L;
        List<OfferSummaryDTO> offerSummaries = Arrays.asList(new OfferSummaryDTO(), new OfferSummaryDTO());
        when(offerService.getOffersByMerchant(merchantId)).thenReturn(offerSummaries);
        
        List<OfferSummaryDTO> result = offerServiceImpl.getOffersByMerchant(merchantId);
        
        assertEquals(2, result.size());
        verify(offerService).getOffersByMerchant(merchantId);
    }

    @Test
    void shouldCreateOffer_whenCreateOfferIsCalled() {
        OfferDTO newOffer = new OfferDTO();
        when(offerService.createOffer(newOffer)).thenReturn(newOffer);
        
        OfferDTO result = offerServiceImpl.createOffer(newOffer);
        
        assertNotNull(result);
        assertEquals(newOffer, result);
        verify(offerService).createOffer(newOffer);
    }

    @Test
    void shouldUpdateOffer_whenUpdateOfferIsCalled() {
        Long offerId = 1L;
        OfferDTO updatedOffer = new OfferDTO();
        when(offerService.updateOffer(offerId, updatedOffer)).thenReturn(updatedOffer);
        
        OfferDTO result = offerServiceImpl.updateOffer(offerId, updatedOffer);
        
        assertNotNull(result);
        assertEquals(updatedOffer, result);
        verify(offerService).updateOffer(offerId, updatedOffer);
    }

    @Test
    void shouldReturnMatchingOffers_whenSearchOffersIsCalled() {
        String keyword = "discount";
        List<OfferDTO> matchingOffers = Arrays.asList(new OfferDTO(), new OfferDTO());
        when(offerService.searchOffers(keyword, null, null)).thenReturn(matchingOffers);
        
        List<OfferDTO> result = offerServiceImpl.searchOffers(keyword, null, null);
        
        assertEquals(2, result.size());
        verify(offerService).searchOffers(keyword, null, null);
    }
}