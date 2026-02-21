package com.cardoffers.oms.mapper;

import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import com.cardoffers.oms.model.dto.MerchantDTO;
import com.cardoffers.oms.model.entity.Merchant;

@ExtendWith(MockitoExtension.class)
public class MerchantMapperTest {

    private final MerchantMapper merchantMapper = Mappers.getMapper(MerchantMapper.class);

    @Test
    public void shouldConvertEntityToDTO_whenEntityIsValid() {
        Merchant entity = new Merchant();
        entity.setId(1L);
        entity.setName("Test Merchant");
        
        MerchantDTO dto = merchantMapper.toDTO(entity);
        
        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
    }

    @Test
    public void shouldConvertDTOToEntity_whenDTOIsValid() {
        MerchantDTO dto = new MerchantDTO();
        dto.setId(1L);
        dto.setName("Test Merchant DTO");
        
        Merchant entity = merchantMapper.toEntity(dto);
        
        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
    }

    @Test
    public void shouldReturnNull_whenEntityIsNull() {
        MerchantDTO dto = merchantMapper.toDTO(null);
        
        assertNull(dto);
    }

    @Test
    public void shouldReturnNull_whenDTOIsNull() {
        Merchant entity = merchantMapper.toEntity(null);
        
        assertNull(entity);
    }
}