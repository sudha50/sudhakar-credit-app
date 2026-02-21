package com.cardoffers.oms.mapper;

import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
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
    private CardholderMapperImpl mapper = Mappers.getMapper(CardholderMapperImpl.class);

    @Test
    void shouldConvertEntityToDTO_whenEntityIsProvided() {
        Cardholder entity = new Cardholder();
        entity.setId(1L);
        entity.setName("John Doe");

        CardholderDTO dto = mapper.toDTO(entity);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("John Doe", dto.getName());
    }

    @Test
    void shouldConvertDTOToEntity_whenDTOIsProvided() {
        CardholderDTO dto = new CardholderDTO();
        dto.setId(1L);
        dto.setName("John Doe");

        Cardholder entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("John Doe", entity.getName());
    }

    @Test
    void shouldReturnEmptyList_whenNoEntitiesProvided() {
        List<CardholderDTO> dtos = mapper.toDTOs(Collections.emptyList());

        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }

    @Test
    void shouldConvertListOfEntitiesToListOfDTOs_whenEntitiesAreProvided() {
        Cardholder entity1 = new Cardholder();
        entity1.setId(1L);
        entity1.setName("John Doe");
        
        Cardholder entity2 = new Cardholder();
        entity2.setId(2L);
        entity2.setName("Jane Doe");

        List<CardholderDTO> dtos = mapper.toDTOs(Arrays.asList(entity1, entity2));

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals(1L, dtos.get(0).getId());
        assertEquals("John Doe", dtos.get(0).getName());
        assertEquals(2L, dtos.get(1).getId());
        assertEquals("Jane Doe", dtos.get(1).getName());
    }
}