package com.cardoffers.oms.mapper;

import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
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
    private CardholderMapperImpl cardholderMapper = Mappers.getMapper(CardholderMapperImpl.class);

    private Cardholder entity;
    private CardholderDTO dto;

    @BeforeEach
    void setUp() {
        entity = new Cardholder();
        entity.setId(1L);
        entity.setName("John Doe");

        dto = new CardholderDTO();
        dto.setId(1L);
        dto.setName("John Doe");
    }

    @Test
    void shouldConvertEntityToDTO_whenEntityIsProvided() {
        CardholderDTO result = cardholderMapper.toDTO(entity);
        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getName(), result.getName());
    }

    @Test
    void shouldConvertDTOToEntity_whenDTOIsProvided() {
        Cardholder result = cardholderMapper.toEntity(dto);
        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getName(), result.getName());
    }

    @Test
    void shouldConvertListOfEntitiesToDTOs_whenEntitiesAreProvided() {
        List<Cardholder> entities = Collections.singletonList(entity);
        List<CardholderDTO> result = cardholderMapper.toDTOs(entities);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(entity.getId(), result.get(0).getId());
        assertEquals(entity.getName(), result.get(0).getName());
    }

    @Test
    void shouldReturnEmptyList_whenNoEntitiesProvidedForDTOConversion() {
        List<CardholderDTO> result = cardholderMapper.toDTOs(Collections.emptyList());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}