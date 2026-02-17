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
    private MerchantMapperImpl merchantMapper = Mappers.getMapper(MerchantMapperImpl.class);

    @Test
    void shouldMapEntityToDTO_whenCalled() {
        // Arrange
        Merchant entity = new Merchant();
        entity.setId(1L);
        entity.setName("Test Merchant");
        
        // Act
        MerchantDTO dto = merchantMapper.toDTO(entity);
        
        // Assert
        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
    }

    @Test
    void shouldMapDTOToEntity_whenCalled() {
        // Arrange
        MerchantDTO dto = new MerchantDTO();
        dto.setId(1L);
        dto.setName("Test Merchant DTO");
        
        // Act
        Merchant entity = merchantMapper.toEntity(dto);
        
        // Assert
        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
    }

    @Test
    void shouldReturnNull_whenEntityIsNullForDTO() {
        // Act
        MerchantDTO dto = merchantMapper.toDTO(null);
        
        // Assert
        assertNull(dto);
    }

    @Test
    void shouldReturnNull_whenDTOIsNullForEntity() {
        // Act
        Merchant entity = merchantMapper.toEntity(null);
        
        // Assert
        assertNull(entity);
    }
}