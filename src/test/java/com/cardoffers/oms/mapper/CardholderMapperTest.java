package com.cardoffers.oms.mapper;

import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cardoffers.oms.model.dto.CardholderDTO;
import com.cardoffers.oms.model.entity.Cardholder;

@ExtendWith(MockitoExtension.class)
class CardholderMapperTest {

    @InjectMocks
    private CardholderMapper cardholderMapper = Mappers.getMapper(CardholderMapper.class);

    @Test
    void shouldConvertToDTO_whenValidCardholderProvided() {
        // Arrange
        Cardholder entity = new Cardholder();
        entity.setId(1L);
        entity.setName("John Doe");

        // Act
        CardholderDTO dto = cardholderMapper.toDTO(entity);

        // Assert
        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
    }

    @Test
    void shouldConvertToEntity_whenValidCardholderDTOProvided() {
        // Arrange
        CardholderDTO dto = new CardholderDTO();
        dto.setId(1L);
        dto.setName("Jane Doe");

        // Act
        Cardholder entity = cardholderMapper.toEntity(dto);

        // Assert
        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
    }

    @Test
    void shouldConvertToDTOs_whenValidCardholderListProvided() {
        // Arrange
        Cardholder entity1 = new Cardholder();
        entity1.setId(1L);
        entity1.setName("Cardholder One");

        Cardholder entity2 = new Cardholder();
        entity2.setId(2L);
        entity2.setName("Cardholder Two");

        List<Cardholder> entities = List.of(entity1, entity2);

        // Act
        List<CardholderDTO> dtos = cardholderMapper.toDTOs(entities);

        // Assert
        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals(entity1.getId(), dtos.get(0).getId());
        assertEquals(entity2.getId(), dtos.get(1).getId());
    }

    @Test
    void shouldReturnEmptyList_whenEmptyCardholderListProvided() {
        // Arrange
        List<Cardholder> entities = List.of();

        // Act
        List<CardholderDTO> dtos = cardholderMapper.toDTOs(entities);

        // Assert
        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }
}