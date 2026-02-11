package com.cardoffers.oms.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    private CardNetworkMapperImpl cardNetworkMapper = Mappers.getMapper(CardNetworkMapper.class);

    @Test
    void shouldConvertToDTO_whenEntityIsProvided() {
        CardNetwork entity = new CardNetwork();
        entity.setId(1L);
        entity.setName("Visa");

        CardNetworkDTO dto = cardNetworkMapper.toDTO(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
    }

    @Test
    void shouldConvertToEntity_whenDTOIsProvided() {
        CardNetworkDTO dto = new CardNetworkDTO();
        dto.setId(1L);
        dto.setName("MasterCard");

        CardNetwork entity = cardNetworkMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
    }

    @Test
    void shouldReturnNull_whenEntityIsNull() {
        CardNetworkDTO dto = cardNetworkMapper.toDTO(null);
        assertEquals(null, dto);
    }

    @Test
    void shouldReturnNull_whenDTOIsNull() {
        CardNetwork entity = cardNetworkMapper.toEntity(null);
        assertEquals(null, entity);
    }

    @Test
    void shouldHandleEmptyValues_whenEntityHasEmptyFields() {
        CardNetwork entity = new CardNetwork();
        entity.setId(null);
        entity.setName("");

        CardNetworkDTO dto = cardNetworkMapper.toDTO(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
    }

    @Test
    void shouldHandleEmptyValues_whenDTOHasEmptyFields() {
        CardNetworkDTO dto = new CardNetworkDTO();
        dto.setId(null);
        dto.setName("");

        CardNetwork entity = cardNetworkMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
    }
}