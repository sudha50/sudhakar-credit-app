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
public class CardholderMapperTest {

    @InjectMocks
    private CardholderMapper mapper = Mappers.getMapper(CardholderMapper.class);

    @Test
    void shouldConvertToDTO_whenEntityIsProvided() {
        // Arrange
        Cardholder cardholder = new Cardholder();
        cardholder.setId(1L);
        cardholder.setName("John Doe");

        // Act
        CardholderDTO dto = mapper.toDTO(cardholder);

        // Assert
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("John Doe", dto.getName());
    }

    @Test
    void shouldConvertToEntity_whenDTOIsProvided() {
        // Arrange
        CardholderDTO dto = new CardholderDTO();
        dto.setId(1L);
        dto.setName("John Doe");

        // Act
        Cardholder entity = mapper.toEntity(dto);

        // Assert
        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("John Doe", entity.getName());
    }

    @Test
    void shouldConvertToDTOs_whenListOfEntitiesIsProvided() {
        // Arrange
        Cardholder cardholder = new Cardholder();
        cardholder.setId(1L);
        cardholder.setName("John Doe");
        List<Cardholder> entities = Collections.singletonList(cardholder);

        // Act
        List<CardholderDTO> dtos = mapper.toDTOs(entities);

        // Assert
        assertNotNull(dtos);
        assertEquals(1, dtos.size());
        assertEquals(1L, dtos.get(0).getId());
        assertEquals("John Doe", dtos.get(0).getName());
    }

    @Test
    void shouldReturnEmptyList_whenNoEntitiesProvided() {
        // Act
        List<CardholderDTO> dtos = mapper.toDTOs(Collections.emptyList());

        // Assert
        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }
}