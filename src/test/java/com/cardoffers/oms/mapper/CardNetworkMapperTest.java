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
    private CardNetworkMapperImpl mapper = Mappers.getMapper(CardNetworkMapper.class);

    @Test
    void shouldConvertEntityToDTO_whenEntityIsProvided() {
        // Arrange
        CardNetwork entity = new CardNetwork();
        entity.setId(1L);
        entity.setName("Visa");

        // Act
        CardNetworkDTO dto = mapper.toDTO(entity);

        // Assert
        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
    }

    @Test
    void shouldConvertDTOToEntity_whenDTOIsProvided() {
        // Arrange
        CardNetworkDTO dto = new CardNetworkDTO();
        dto.setId(1L);
        dto.setName("MasterCard");

        // Act
        CardNetwork entity = mapper.toEntity(dto);

        // Assert
        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
    }

    @Test
    void shouldReturnNull_whenEntityIsNullToDTO() {
        // Act
        CardNetworkDTO dto = mapper.toDTO(null);

        // Assert
        assertNull(dto);
    }

    @Test
    void shouldReturnNull_whenDTOIsNullToEntity() {
        // Act
        CardNetwork entity = mapper.toEntity(null);

        // Assert
        assertNull(entity);
    }

    @Test
    void shouldHandleEmptyFieldsInEntity_whenConvertingToDTO() {
        // Arrange
        CardNetwork entity = new CardNetwork();
        entity.setId(2L);
        entity.setName("");

        // Act
        CardNetworkDTO dto = mapper.toDTO(entity);

        // Assert
        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName()); // Check if empty is correctly mapped
    }

    @Test
    void shouldHandleEmptyFieldsInDTO_whenConvertingToEntity() {
        // Arrange
        CardNetworkDTO dto = new CardNetworkDTO();
        dto.setId(2L);
        dto.setName("");

        // Act
        CardNetwork entity = mapper.toEntity(dto);

        // Assert
        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName()); // Check if empty is correctly mapped
    }
}