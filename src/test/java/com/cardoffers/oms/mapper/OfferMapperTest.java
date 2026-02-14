package com.cardoffers.oms.mapper;

import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cardoffers.oms.model.dto.OfferDTO;
import com.cardoffers.oms.model.dto.OfferSummaryDTO;
import com.cardoffers.oms.model.entity.Offer;

@ExtendWith(MockitoExtension.class)
public class OfferMapperTest {

    @InjectMocks
    private OfferMapperImpl offerMapper;

    @Test
    void shouldMapEntityToDTO_whenEntityIsProvided() {
        Offer offer = anOffer();
        OfferDTO dto = offerMapper.toDTO(offer);
        
        assertNotNull(dto);
        assertEquals(offer.getTitle(), dto.getTitle());
        assertEquals(offer.getDescription(), dto.getDescription());
    }

    @Test
    void shouldMapDTOToEntity_whenDTOIsProvided() {
        OfferDTO dto = anOfferDTO();
        Offer offer = offerMapper.toEntity(dto);
        
        assertNotNull(offer);
        assertEquals(dto.getTitle(), offer.getTitle());
        assertEquals(dto.getDescription(), offer.getDescription());
    }

    @Test
    void shouldMapEntityToSummaryDTO_whenEntityIsProvided() {
        Offer offer = anOffer();
        offer.getMerchant().setName("Test Merchant");
        OfferSummaryDTO summaryDTO = offerMapper.toSummaryDTO(offer);
        
        assertNotNull(summaryDTO);
        assertEquals("Test Merchant", summaryDTO.getMerchantName());
    }

    private static Offer anOffer() {
        Offer entity = new Offer();
        entity.setId(1L);
        entity.setTitle("Test Title");
        entity.setDescription("Test description");
        entity.setOfferType("DEFAULT");
        entity.setDiscountPercentage(1);
        entity.setCashbackAmount(new java.math.BigDecimal("100.00"));
        entity.setMerchant(new Merchant());
        entity.getMerchant().setName("Test Merchant");
        entity.setCardNetwork(new CardNetwork());
        entity.setSource("Test Source");
        entity.setMaxRedemptions(1);
        entity.setCurrentRedemptions(1);
        entity.setCreatedAt(java.time.LocalDateTime.now());
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
        entity.setMerchant(null);  // set MerchantDTO accordingly
        entity.setCardNetwork(null); // set CardNetworkDTO accordingly
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
}