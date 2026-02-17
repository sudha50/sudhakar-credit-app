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

import com.cardoffers.oms.model.dto.CardNetworkDTO;
import com.cardoffers.oms.model.entity.CardNetwork;

@ExtendWith(MockitoExtension.class)
class CardNetworkMapperTest {

    @InjectMocks
    private CardNetworkMapperImpl mapper = Mappers.getMapper(CardNetworkMapper.class); // MapStruct generates this implementation

    @Test
    void shouldMapEntityToDTO_whenValidEntityProvided() {
        // Given
        CardNetwork entity = new CardNetwork();
        entity.setId(1L);
        entity.setName("Visa");

        // When
        CardNetworkDTO dto = mapper.toDTO(entity);

        // Then
        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
    }

    @Test
    void shouldMapDTOToEntity_whenValidDTOProvided() {
        // Given
        CardNetworkDTO dto = new CardNetworkDTO();
        dto.setId(1L);
        dto.setName("MasterCard");

        // When
        CardNetwork entity = mapper.toEntity(dto);

        // Then
        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
    }

    @Test
    void shouldReturnNull_whenNullEntityProvidedToDTO() {
        // When
        CardNetworkDTO dto = mapper.toDTO(null);

        // Then
        assertNull(dto);
    }

    @Test
    void shouldReturnNull_whenNullDTOProvidedToEntity() {
        // When
        CardNetwork entity = mapper.toEntity(null);

        // Then
        assertNull(entity);
    }
}