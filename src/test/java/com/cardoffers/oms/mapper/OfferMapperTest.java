package com.cardoffers.oms.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    private OfferMapperImpl offerMapper; // Assuming you have an implementation named OfferMapperImpl

    @Mock
    private MerchantMapper merchantMapper;

    @Test
    void shouldConvertToDTO_whenEntityIsValid() {
        Offer offer = new Offer();
        offer.setId(1L);
        offer.setDescription("Test Offer");
        
        OfferDTO result = offerMapper.toDTO(offer);
        
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Offer", result.getDescription());
    }

    @Test
    void shouldConvertToEntity_whenDTOIsValid() {
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setId(1L);
        offerDTO.setDescription("Test Offer DTO");
        
        Offer result = offerMapper.toEntity(offerDTO);
        
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Offer DTO", result.getDescription());
    }

    @Test
    void shouldConvertToSummaryDTO_whenEntityIsValid() {
        Merchant merchant = new Merchant();
        merchant.setName("Test Merchant");
        
        Offer offer = new Offer();
        offer.setId(1L);
        offer.setMerchant(merchant);
        
        OfferSummaryDTO result = offerMapper.toSummaryDTO(offer);
        
        assertNotNull(result);
        assertEquals("Test Merchant", result.getMerchantName());
    }

    @Test
    void shouldReturnNull_whenEntityIsNullForToDTO() {
        OfferDTO result = offerMapper.toDTO(null);
        
        assertEquals(null, result);
    }

    @Test
    void shouldReturnNull_whenDTOIsNullForToEntity() {
        Offer result = offerMapper.toEntity(null);
        
        assertEquals(null, result);
    }

    @Test
    void shouldReturnNull_whenEntityIsNullForToSummaryDTO() {
        OfferSummaryDTO result = offerMapper.toSummaryDTO(null);

        assertEquals(null, result);
    }
}