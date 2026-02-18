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
import com.cardoffers.oms.model.entity.Merchant;

@ExtendWith(MockitoExtension.class)
class OfferMapperTest {

    @InjectMocks
    private OfferMapperImpl offerMapper;

    @Mock
    private MerchantMapper merchantMapper;

    @Mock
    private CardNetworkMapper cardNetworkMapper;

    @Test
    void shouldMapEntityToDTO_whenValidEntityProvided() {
        Offer offer = new Offer();
        offer.setId(1L);
        offer.setName("Test Offer");
        offer.setMerchant(new Merchant());

        OfferDTO result = offerMapper.toDTO(offer);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Offer", result.getName());
    }

    @Test
    void shouldMapDTOToEntity_whenValidDTOProvided() {
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setId(1L);
        offerDTO.setName("Test Offer DTO");

        Offer result = offerMapper.toEntity(offerDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Offer DTO", result.getName());
    }

    @Test
    void shouldMapEntityToSummaryDTO_whenValidEntityProvided() {
        Offer offer = new Offer();
        offer.setId(1L);
        offer.setMerchant(new Merchant());
        offer.getMerchant().setName("Merchant Name");

        OfferSummaryDTO result = offerMapper.toSummaryDTO(offer);

        assertNotNull(result);
        assertEquals("Merchant Name", result.getMerchantName());
    }

    @Test
    void shouldReturnNull_whenMappingNullEntityToDTO() {
        OfferDTO result = offerMapper.toDTO(null);
        assertNull(result);
    }

    @Test
    void shouldReturnNull_whenMappingNullDTOToEntity() {
        Offer result = offerMapper.toEntity(null);
        assertNull(result);
    }

    @Test
    void shouldReturnNull_whenMappingNullEntityToSummaryDTO() {
        OfferSummaryDTO result = offerMapper.toSummaryDTO(null);
        assertNull(result);
    }
}