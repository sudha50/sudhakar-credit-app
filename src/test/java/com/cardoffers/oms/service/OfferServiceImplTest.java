package com.cardoffers.oms.service;

import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cardoffers.oms.exception.InvalidOfferException;
import com.cardoffers.oms.exception.ResourceNotFoundException;
import com.cardoffers.oms.mapper.OfferMapper;
import com.cardoffers.oms.model.dto.OfferDTO;
import com.cardoffers.oms.model.dto.OfferSummaryDTO;
import com.cardoffers.oms.model.entity.CardNetwork;
import com.cardoffers.oms.model.entity.Merchant;
import com.cardoffers.oms.model.entity.Offer;
import com.cardoffers.oms.repository.CardNetworkRepository;
import com.cardoffers.oms.repository.MerchantRepository;
import com.cardoffers.oms.repository.OfferRepository;

@ExtendWith(MockitoExtension.class)
class OfferServiceImplTest {

    @Mock
    OfferRepository offerRepository;

    @Mock
    MerchantRepository merchantRepository;

    @Mock
    CardNetworkRepository cardNetworkRepository;

    @Mock
    OfferMapper offerMapper;

    @InjectMocks
    OfferServiceImpl offerServiceImpl;

    private static CardNetwork aCardNetwork() {
        CardNetwork entity = new CardNetwork();
        entity.setId(1L);
        entity.setName("TestName");
        entity.setCode("TEST001");
        return entity;
    }

    private static Merchant aMerchant() {
        Merchant entity = new Merchant();
        entity.setId(1L);
        entity.setName("TestName");
        entity.setDescription("Test description");
        entity.setCategory("test-value");
        entity.setLogoUrl("https://example.com");
        entity.setWebsite("test-value");
        return entity;
    }

    private static Offer anOffer() {
        Offer entity = new Offer();
        entity.setId(1L);
        entity.setTitle("Test Title");
        entity.setDescription("Test description");
        entity.setOfferType("DEFAULT");
        entity.setDiscountPercentage(1);
        entity.setCashbackAmount(new java.math.BigDecimal("100.00"));
        entity.setMerchant(aMerchant());
        entity.setCardNetwork(aCardNetwork());
        entity.setSource("test-source");
        entity.setMaxRedemptions(1);
        entity.setCurrentRedemptions(0);
        entity.setCreatedAt(LocalDate.now());
        return entity;
    }

    private static OfferDTO anOfferDTO() {
        OfferDTO dto = new OfferDTO();
        dto.setTitle("Test Title");
        dto.setDescription("Test description");
        dto.setOfferType("DEFAULT");
        dto.setDiscountPercentage(1);
        dto.setCashbackAmount(new java.math.BigDecimal("100.00"));
        dto.setMinimumPurchaseAmount(new java.math.BigDecimal("100.00"));
        dto.setStartDate(LocalDate.of(2025, 1, 15));
        dto.setEndDate(LocalDate.of(2025, 1, 15));
        dto.setTermsAndConditions("test-value");
        dto.setMerchant(null); // Set MerchantDTO
        dto.setCardNetwork(null); // Set CardNetworkDTO
        dto.setSource("test-value");
        dto.setMaxRedemptions(1);
        dto.setCurrentRedemptions(1);
        dto.setActive(true);
        return dto;
    }

    @Test
    void shouldReturnOfferDTO_whenOfferExists() {
        Offer offer = anOffer();
        OfferDTO expectedDto = anOfferDTO();
        
        when(offerRepository.findById(1L)).thenReturn(Optional.of(offer));
        when(offerMapper.toDTO(offer)).thenReturn(expectedDto);
        
        OfferDTO result = offerServiceImpl.getOfferById(1L);
        
        assertNotNull(result);
        assertEquals(expectedDto.getTitle(), result.getTitle());
        verify(offerRepository).findById(1L);
    }

    @Test
    void shouldThrowResourceNotFoundException_whenOfferDoesNotExist() {
        when(offerRepository.findById(any())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> 
            offerServiceImpl.getOfferById(1L)
        );

        assertEquals("Offer not found", exception.getMessage());
    }

    @Test
    void shouldReturnOfferSummaries_whenMerchantExists() {
        Offer offer = anOffer();
        OfferSummaryDTO summaryDto = new OfferSummaryDTO();
        
        when(offerRepository.findActiveOffersByMerchant(1L)).thenReturn(Collections.singletonList(offer));
        when(offerMapper.toSummaryDTO(offer)).thenReturn(summaryDto);
        
        List<OfferSummaryDTO> result = offerServiceImpl.getOffersByMerchant(1L);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(summaryDto, result.get(0));
    }

    @Test
    void shouldThrowResourceNotFoundException_whenMerchantDoesNotExistForCreate() {
        OfferDTO dto = anOfferDTO();
        when(merchantRepository.findById(dto.getMerchant().getId())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> 
            offerServiceImpl.createOffer(dto)
        );

        assertEquals("Merchant not found", exception.getMessage());
    }

    @Test
    void shouldThrowInvalidOfferException_whenEndDateBeforeStartDate() {
        OfferDTO dto = anOfferDTO();
        dto.setStartDate(LocalDate.of(2025, 1, 16));
        dto.setEndDate(LocalDate.of(2025, 1, 15));

        InvalidOfferException exception = assertThrows(InvalidOfferException.class, () -> 
            offerServiceImpl.createOffer(dto)
        );

        assertEquals("Offer endDate must be on/after startDate", exception.getMessage());
    }

    @Test
    void shouldUpdateOffer_whenValidDTOIsProvided() {
        Offer existingOffer = anOffer();
        OfferDTO dto = anOfferDTO();
        
        when(offerRepository.findById(1L)).thenReturn(Optional.of(existingOffer));
        when(merchantRepository.findById(dto.getMerchant().getId())).thenReturn(Optional.of(aMerchant()));
        when(cardNetworkRepository.findById(dto.getCardNetwork().getId())).thenReturn(Optional.of(aCardNetwork()));
        when(offerRepository.save(existingOffer)).thenReturn(existingOffer);
        
        OfferDTO result = offerServiceImpl.updateOffer(1L, dto);
        
        assertNotNull(result);
        verify(offerRepository).save(existingOffer);
    }

    @Test
    void shouldSearchOffers_whenValidKeywordIsProvided() {
        Offer offer = anOffer();
        OfferDTO expectedDto = anOfferDTO();
        
        when(offerRepository.findActiveOffers(LocalDate.now())).thenReturn(Collections.singletonList(offer));
        when(offerMapper.toDTO(offer)).thenReturn(expectedDto);
        
        List<OfferDTO> result = offerServiceImpl.searchOffers("Test", null, null);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedDto, result.get(0));
    }
}