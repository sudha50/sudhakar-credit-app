package com.cardoffers.oms.mapper;

import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
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
    private CardholderMapperImpl cardholderMapper = Mappers.getMapper(CardholderMapper.class);

    @Test
    void shouldConvertCardholderToDTO_whenValidEntity() {
        // Arrange
        Cardholder cardholder = new Cardholder();
        cardholder.setId(1L);
        cardholder.setName("John Doe");

        // Act
        CardholderDTO dto = cardholderMapper.toDTO(cardholder);

        // Assert
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("John Doe", dto.getName());
    }

    @Test
    void shouldConvertDTOToCardholder_whenValidDTO() {
        // Arrange
        CardholderDTO dto = new CardholderDTO();
        dto.setId(1L);
        dto.setName("John Doe");

        // Act
        Cardholder cardholder = cardholderMapper.toEntity(dto);

        // Assert
        assertNotNull(cardholder);
        assertEquals(1L, cardholder.getId());
        assertEquals("John Doe", cardholder.getName());
    }

    @Test
    void shouldConvertListOfCardholdersToDTOs_whenValidEntities() {
        // Arrange
        Cardholder cardholder = new Cardholder();
        cardholder.setId(1L);
        cardholder.setName("John Doe");
        List<Cardholder> entities = Collections.singletonList(cardholder);

        // Act
        List<CardholderDTO> dtos = cardholderMapper.toDTOs(entities);

        // Assert
        assertNotNull(dtos);
        assertEquals(1, dtos.size());
        assertEquals(1L, dtos.get(0).getId());
        assertEquals("John Doe", dtos.get(0).getName());
    }

    @Test
    void shouldReturnEmptyList_whenNoEntitiesProvided() {
        // Arrange
        List<Cardholder> entities = Collections.emptyList();

        // Act
        List<CardholderDTO> dtos = cardholderMapper.toDTOs(entities);

        // Assert
        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }
}