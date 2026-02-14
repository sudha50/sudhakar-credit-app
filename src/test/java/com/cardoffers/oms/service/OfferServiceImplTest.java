package com.cardoffers.oms.service;

import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
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

import java.util.List;

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
        entity.setCashbackAmount(new BigDecimal("100.00"));
        entity.setMerchant(aMerchant());
        entity.setCardNetwork(aCardNetwork());
        entity.setSource("source");
        entity.setMaxRedemptions(1);
        entity.setCurrentRedemptions(0);
        entity.setCreatedAt(LocalDate.now().atStartOfDay());
        return entity;
    }

    private static OfferDTO anOfferDTO() {
        OfferDTO dto = new OfferDTO();
        dto.setTitle("Test Title");
        dto.setDescription("Test description");
        dto.setOfferType("DEFAULT");
        dto.setDiscountPercentage(1);
        dto.setCashbackAmount(new BigDecimal("100.00"));
        dto.setMinimumPurchaseAmount(new BigDecimal("100.00"));
        dto.setStartDate(LocalDate.of(2025, 1, 15));
        dto.setEndDate(LocalDate.of(2025, 1, 16));
        dto.setTermsAndConditions("test-value");
        dto.setMerchant(aMerchant());
        dto.setCardNetwork(aCardNetwork());
        dto.setSource("test-value");
        dto.setMaxRedemptions(1);
        dto.setCurrentRedemptions(0);
        dto.setActive(true);
        return dto;
    }

    @Test
    void shouldReturnOfferDTO_whenGetOfferByIdIsCalled() {
        Offer offer = anOffer();
        OfferDTO offerDTO = anOfferDTO();

        when(offerRepository.findById(1L)).thenReturn(Optional.of(offer));
        when(offerMapper.toDTO(offer)).thenReturn(offerDTO);

        OfferDTO result = offerServiceImpl.getOfferById(1L);

        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());
    }

    @Test
    void shouldThrowException_whenGetOfferByIdIsCalledWithInvalidId() {
        when(offerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> offerServiceImpl.getOfferById(1L));
    }

    @Test
    void shouldReturnOfferSummaries_whenGetOffersByMerchantIsCalled() {
        List<Offer> offers = Arrays.asList(anOffer());
        List<OfferSummaryDTO> summaries = List.of(new OfferSummaryDTO());

        when(offerRepository.findActiveOffersByMerchant(1L)).thenReturn(offers);
        when(offerMapper.toSummaryDTO(any())).thenReturn(new OfferSummaryDTO());

        List<OfferSummaryDTO> result = offerServiceImpl.getOffersByMerchant(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void shouldCreateOffer_whenCreateOfferIsCalled() {
        OfferDTO dto = anOfferDTO();
        Offer offer = anOffer();

        when(merchantRepository.findById(anyLong())).thenReturn(Optional.of(aMerchant()));
        when(cardNetworkRepository.findById(anyLong())).thenReturn(Optional.of(aCardNetwork()));
        when(offerMapper.toEntity(dto)).thenReturn(offer);
        when(offerRepository.save(offer)).thenReturn(offer);
        when(offerMapper.toDTO(offer)).thenReturn(dto);

        OfferDTO result = offerServiceImpl.createOffer(dto);

        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());
    }

    @Test
    void shouldThrowException_whenCreateOfferIsCalledWithInvalidMerchant() {
        OfferDTO dto = anOfferDTO();
        when(merchantRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> offerServiceImpl.createOffer(dto));
    }

    @Test
    void shouldThrowInvalidOfferException_whenOfferEndDateBeforeStartDate() {
        OfferDTO dto = anOfferDTO();
        dto.setStartDate(LocalDate.of(2025, 1, 16));
        dto.setEndDate(LocalDate.of(2025, 1, 15));

        assertThrows(InvalidOfferException.class, () -> offerServiceImpl.createOffer(dto));
    }

    @Test
    void shouldUpdateOffer_whenUpdateOfferIsCalled() {
        OfferDTO dto = anOfferDTO();
        Offer existingOffer = anOffer();

        when(offerRepository.findById(1L)).thenReturn(Optional.of(existingOffer));
        when(merchantRepository.findById(anyLong())).thenReturn(Optional.of(aMerchant()));
        when(cardNetworkRepository.findById(anyLong())).thenReturn(Optional.of(aCardNetwork()));
        when(offerMapper.toDTO(existingOffer)).thenReturn(dto);

        OfferDTO result = offerServiceImpl.updateOffer(1L, dto);

        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());
    }
}