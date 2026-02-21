package com.cardoffers.oms.mapper;

import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
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
    private OfferMapperImpl offerMapper; // Assuming that OfferMapperImpl is the generated implementation.

    @Mock
    private MerchantMapper merchantMapper;

    @Test
    void shouldConvertToDTO_whenEntityIsProvided() {
        Offer entity = new Offer();
        entity.setId(1L);
        entity.setDescription("Test Offer");
        Merchant merchant = new Merchant();
        merchant.setId(1L);
        merchant.setName("Test Merchant");
        entity.setMerchant(merchant);

        OfferDTO dto = offerMapper.toDTO(entity);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Test Offer", dto.getDescription());
        assertEquals("Test Merchant", dto.getMerchantName());
    }

    @Test
    void shouldConvertToEntity_whenDTOIsProvided() {
        OfferDTO dto = new OfferDTO();
        dto.setId(1L);
        dto.setDescription("Test Offer");

        Offer entity = offerMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("Test Offer", entity.getDescription());
    }

    @Test
    void shouldConvertToSummaryDTO_whenEntityIsProvided() {
        Offer entity = new Offer();
        entity.setId(1L);
        Merchant merchant = new Merchant();
        merchant.setId(1L);
        merchant.setName("Test Merchant");
        entity.setMerchant(merchant);

        OfferSummaryDTO summaryDTO = offerMapper.toSummaryDTO(entity);

        assertNotNull(summaryDTO);
        assertEquals("Test Merchant", summaryDTO.getMerchantName());
    }

    @Test
    void shouldReturnNull_whenEntityToDTOIsNull() {
        OfferDTO dto = offerMapper.toDTO(null);
        assertNull(dto);
    }

    @Test
    void shouldReturnNull_whenDTOToEntityIsNull() {
        Offer entity = offerMapper.toEntity(null);
        assertNull(entity);
    }

    @Test
    void shouldReturnNull_whenEntityToSummaryDTOIsNull() {
        OfferSummaryDTO summaryDTO = offerMapper.toSummaryDTO(null);
        assertNull(summaryDTO);
    }
}