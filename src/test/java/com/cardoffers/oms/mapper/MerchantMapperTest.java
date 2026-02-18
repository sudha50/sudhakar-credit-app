package com.cardoffers.oms.mapper;

import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cardoffers.oms.model.dto.MerchantDTO;
import com.cardoffers.oms.model.entity.Merchant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MerchantMapperTest {

    @InjectMocks
    private MerchantMapperImpl merchantMapper;

    @Test
    void shouldConvertEntityToDTO_whenValidMerchantProvided() {
        // Arrange
        Merchant merchant = new Merchant();
        merchant.setId(1L);
        merchant.setName("Test Merchant");

        // Act
        MerchantDTO dto = merchantMapper.toDTO(merchant);

        // Assert
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Test Merchant", dto.getName());
    }

    @Test
    void shouldConvertDTOToEntity_whenValidMerchantDTOProvided() {
        // Arrange
        MerchantDTO dto = new MerchantDTO();
        dto.setId(1L);
        dto.setName("Test Merchant DTO");

        // Act
        Merchant entity = merchantMapper.toEntity(dto);

        // Assert
        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("Test Merchant DTO", entity.getName());
    }

    @Test
    void shouldReturnNull_whenNullEntityProvidedToDTO() {
        // Act
        MerchantDTO dto = merchantMapper.toDTO(null);

        // Assert
        assertNull(dto);
    }

    @Test
    void shouldReturnNull_whenNullDTOProvidedToEntity() {
        // Act
        Merchant entity = merchantMapper.toEntity(null);

        // Assert
        assertNull(entity);
    }

    @Test
    void shouldHandleEmptyName_whenMappingEntityToDTO() {
        // Arrange
        Merchant merchant = new Merchant();
        merchant.setId(1L);
        merchant.setName("");

        // Act
        MerchantDTO dto = merchantMapper.toDTO(merchant);

        // Assert
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("", dto.getName());
    }

    @Test
    void shouldHandleEmptyName_whenMappingDTOToEntity() {
        // Arrange
        MerchantDTO dto = new MerchantDTO();
        dto.setId(1L);
        dto.setName("");

        // Act
        Merchant entity = merchantMapper.toEntity(dto);

        // Assert
        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("", entity.getName());
    }
}