package com.cardoffers.oms.mapper;

import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.cardoffers.oms.model.dto.CardNetworkDTO;
import com.cardoffers.oms.model.entity.CardNetwork;

@ExtendWith(MockitoExtension.class)
class CardNetworkMapperTest {

    private final CardNetworkMapper mapper = Mappers.getMapper(CardNetworkMapper.class);

    @Test
    void shouldConvertEntityToDTO_whenEntityIsValid() {
        CardNetwork entity = new CardNetwork();
        entity.setId(1L);
        entity.setName("Visa");
        
        CardNetworkDTO dto = mapper.toDTO(entity);
        
        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
    }

    @Test
    void shouldConvertDTOToEntity_whenDTOIsValid() {
        CardNetworkDTO dto = new CardNetworkDTO();
        dto.setId(1L);
        dto.setName("MasterCard");
        
        CardNetwork entity = mapper.toEntity(dto);
        
        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
    }

    @Test
    void shouldReturnNull_whenEntityIsNull() {
        CardNetworkDTO dto = mapper.toDTO(null);
        assertNull(dto);
    }

    @Test
    void shouldReturnNull_whenDTONull() {
        CardNetwork entity = mapper.toEntity(null);
        assertNull(entity);
    }

    @Test
    void shouldConvertEntityWithAllFields_whenEntityHasAllFields() {
        CardNetwork entity = new CardNetwork();
        entity.setId(2L);
        entity.setName("American Express");
        
        CardNetworkDTO dto = mapper.toDTO(entity);
        
        assertNotNull(dto);
        assertEquals(2L, dto.getId());
        assertEquals("American Express", dto.getName());
    }

    @Test
    void shouldConvertDTOWithAllFields_whenDTOHasAllFields() {
        CardNetworkDTO dto = new CardNetworkDTO();
        dto.setId(3L);
        dto.setName("Discover");
        
        CardNetwork entity = mapper.toEntity(dto);
        
        assertNotNull(entity);
        assertEquals(3L, entity.getId());
        assertEquals("Discover", entity.getName());
    }
}