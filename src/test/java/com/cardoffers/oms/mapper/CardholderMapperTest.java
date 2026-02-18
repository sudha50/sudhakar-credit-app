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
    private CardholderMapperImpl cardholderMapper = Mappers.getMapper(CardholderMapper.class);

    @Test
    public void shouldMapToDTO_whenEntityIsProvided() {
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
    public void shouldMapToEntity_whenDTOIsProvided() {
        // Arrange
        CardholderDTO dto = new CardholderDTO();
        dto.setId(1L);
        dto.setName("John Doe");
        
        // Act
        Cardholder entity = cardholderMapper.toEntity(dto);
        
        // Assert
        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
    }

    @Test
    public void shouldMapToDTOs_whenEntitiesListIsProvided() {
        // Arrange
        Cardholder entity = new Cardholder();
        entity.setId(1L);
        entity.setName("John Doe");
        List<Cardholder> entities = Collections.singletonList(entity);
        
        // Act
        List<CardholderDTO> dtos = cardholderMapper.toDTOs(entities);
        
        // Assert
        assertNotNull(dtos);
        assertEquals(1, dtos.size());
        assertEquals(entity.getId(), dtos.get(0).getId());
        assertEquals(entity.getName(), dtos.get(0).getName());
    }
    
    @Test
    public void shouldReturnEmptyList_whenNoEntitiesProvided() {
        // Arrange
        List<Cardholder> entities = Collections.emptyList();
        
        // Act
        List<CardholderDTO> dtos = cardholderMapper.toDTOs(entities);
        
        // Assert
        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }

    @Test
    public void shouldHandleNullEntity_whenMappingToDTO() {
        // Act
        CardholderDTO dto = cardholderMapper.toDTO(null);
        
        // Assert
        assertNull(dto);
    }

    @Test
    public void shouldHandleNullDTO_whenMappingToEntity() {
        // Act
        Cardholder entity = cardholderMapper.toEntity(null);
        
        // Assert
        assertNull(entity);
    }
}