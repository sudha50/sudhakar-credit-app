package com.cardoffers.oms.mapper;

import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cardoffers.oms.model.dto.MerchantDTO;
import com.cardoffers.oms.model.entity.Merchant;

@ExtendWith(MockitoExtension.class)
class MerchantMapperTest {

    @InjectMocks
    private MerchantMapper merchantMapper = Mappers.getMapper(MerchantMapper.class);

    @Test
    void shouldConvertEntityToDTO_whenEntityIsValid() {
        Merchant entity = new Merchant();
        entity.setId(1L);
        entity.setName("Test Merchant");

        MerchantDTO dto = merchantMapper.toDTO(entity);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Test Merchant", dto.getName());
    }

    @Test
    void shouldConvertDTOToEntity_whenDTOIsValid() {
        MerchantDTO dto = new MerchantDTO();
        dto.setId(2L);
        dto.setName("Another Merchant");

        Merchant entity = merchantMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(2L, entity.getId());
        assertEquals("Another Merchant", entity.getName());
    }

    @Test
    void shouldReturnNull_whenEntityIsNullForDTOConversion() {
        MerchantDTO dto = merchantMapper.toDTO(null);

        assertNull(dto);
    }

    @Test
    void shouldReturnNull_whenDTOIsNullForEntityConversion() {
        Merchant entity = merchantMapper.toEntity(null);

        assertNull(entity);
    }
}