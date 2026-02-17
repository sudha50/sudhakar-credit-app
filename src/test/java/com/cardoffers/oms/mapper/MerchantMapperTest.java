package com.cardoffers.oms.mapper;

import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.cardoffers.oms.model.dto.MerchantDTO;
import com.cardoffers.oms.model.entity.Merchant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MerchantMapperTest {

    private final MerchantMapper mapper = Mappers.getMapper(MerchantMapper.class);

    @Test
    void shouldConvertEntityToDTO_whenEntityIsValid() {
        Merchant entity = new Merchant();
        entity.setId(1L);
        entity.setName("Valid Merchant");

        MerchantDTO dto = mapper.toDTO(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
    }

    @Test
    void shouldConvertDTOToEntity_whenDTOIsValid() {
        MerchantDTO dto = new MerchantDTO();
        dto.setId(1L);
        dto.setName("Valid Merchant DTO");

        Merchant entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
    }

    @Test
    void shouldHandleNullEntity_whenConvertingToDTO() {
        MerchantDTO dto = mapper.toDTO(null);

        assertNull(dto);
    }

    @Test
    void shouldHandleNullDTO_whenConvertingToEntity() {
        Merchant entity = mapper.toEntity(null);

        assertNull(entity);
    }

    @Test
    void shouldConvertEntityToDTO_whenEntityHasEmptyFields() {
        Merchant entity = new Merchant();
        entity.setId(1L);
        entity.setName("");

        MerchantDTO dto = mapper.toDTO(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
    }

    @Test
    void shouldConvertDTOToEntity_whenDTOHasEmptyFields() {
        MerchantDTO dto = new MerchantDTO();
        dto.setId(1L);
        dto.setName("");

        Merchant entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
    }
}