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
import org.mockito.junit.jupiter.MockitoExtension;

import com.cardoffers.oms.model.dto.CardNetworkDTO;
import com.cardoffers.oms.model.entity.CardNetwork;

@ExtendWith(MockitoExtension.class)
class CardNetworkMapperTest {

    @InjectMocks
    CardNetworkMapper cardNetworkMapper = Mappers.getMapper(CardNetworkMapper.class);

    @Test
    void shouldConvertEntityToDTO_whenEntityIsProvided() {
        CardNetwork entity = new CardNetwork();
        entity.setId(1L);
        entity.setName("Visa");

        CardNetworkDTO dto = cardNetworkMapper.toDTO(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
    }

    @Test
    void shouldConvertDTOToEntity_whenDTOIsProvided() {
        CardNetworkDTO dto = new CardNetworkDTO();
        dto.setId(1L);
        dto.setName("Mastercard");

        CardNetwork entity = cardNetworkMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
    }

    @Test
    void shouldReturnSameValues_whenDTOHasNoNulls() {
        CardNetworkDTO dto = new CardNetworkDTO();
        dto.setId(2L);
        dto.setName("American Express");

        CardNetwork entity = cardNetworkMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
    }

    @Test
    void shouldReturnNullDTO_whenNullEntityIsProvided() {
        CardNetworkDTO dto = cardNetworkMapper.toDTO(null);
        assertNull(dto);
    }

    @Test
    void shouldReturnNullEntity_whenNullDTOIsProvided() {
        CardNetwork entity = cardNetworkMapper.toEntity(null);
        assertNull(entity);
    }
}