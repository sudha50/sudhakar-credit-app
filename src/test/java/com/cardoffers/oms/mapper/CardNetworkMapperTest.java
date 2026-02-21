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
    private CardNetworkMapper cardNetworkMapper = Mappers.getMapper(CardNetworkMapper.class);

    @Test
    void shouldConvertEntityToDTO_whenEntityIsProvided() {
        // Arrange
        CardNetwork entity = new CardNetwork();
        entity.setId(1L);
        entity.setName("Visa");

        // Act
        CardNetworkDTO dto = cardNetworkMapper.toDTO(entity);

        // Assert
        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
    }

    @Test
    void shouldConvertDTOToEntity_whenDTOIsProvided() {
        // Arrange
        CardNetworkDTO dto = new CardNetworkDTO();
        dto.setId(2L);
        dto.setName("MasterCard");

        // Act
        CardNetwork entity = cardNetworkMapper.toEntity(dto);

        // Assert
        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
    }

    @Test
    void shouldReturnNull_whenEntityIsNull() {
        // Act
        CardNetworkDTO dto = cardNetworkMapper.toDTO(null);

        // Assert
        assertNull(dto);
    }

    @Test
    void shouldReturnNull_whenDTOIsNull() {
        // Act
        CardNetwork entity = cardNetworkMapper.toEntity(null);

        // Assert
        assertNull(entity);
    }

    @Test
    void shouldMapAllFields_whenFullEntityIsProvided() {
        // Arrange
        CardNetwork entity = new CardNetwork();
        entity.setId(3L);
        entity.setName("American Express");
        
        // Act
        CardNetworkDTO dto = cardNetworkMapper.toDTO(entity);

        // Assert
        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
    }

    @Test
    void shouldMapAllFields_whenFullDTOIsProvided() {
        // Arrange
        CardNetworkDTO dto = new CardNetworkDTO();
        dto.setId(4L);
        dto.setName("Discover");

        // Act
        CardNetwork entity = cardNetworkMapper.toEntity(dto);

        // Assert
        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
    }
}