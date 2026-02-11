package com.cardoffers.oms.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.cardoffers.oms.model.dto.MerchantDTO;
import com.cardoffers.oms.model.entity.Merchant;

@ExtendWith(MockitoExtension.class)
public class MerchantMapperTest {

    private final MerchantMapper mapper = Mappers.getMapper(MerchantMapper.class);

    @Test
    void shouldReturnMerchantDTO_whenMappedFromMerchant() {
        // Arrange
        Merchant merchant = new Merchant();
        merchant.setId(1L);
        merchant.setName("Test Merchant");

        // Act
        MerchantDTO result = mapper.toDTO(merchant);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Merchant", result.getName());
    }

    @Test
    void shouldReturnMerchant_whenMappedFromMerchantDTO() {
        // Arrange
        MerchantDTO dto = new MerchantDTO();
        dto.setId(1L);
        dto.setName("Test Merchant DTO");

        // Act
        Merchant result = mapper.toEntity(dto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Merchant DTO", result.getName());
    }

    @Test
    void shouldReturnNull_whenMerchantIsNull() {
        // Act
        MerchantDTO result = mapper.toDTO(null);

        // Assert
        assertEquals(null, result);
    }

    @Test
    void shouldReturnNull_whenMerchantDTOIsNull() {
        // Act
        Merchant result = mapper.toEntity(null);

        // Assert
        assertEquals(null, result);
    }
}