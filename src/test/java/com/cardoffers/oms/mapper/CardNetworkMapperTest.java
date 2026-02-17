package com.cardoffers.oms.mapper;

import org.mockito.Mockito;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cardoffers.oms.model.dto.CardNetworkDTO;
import com.cardoffers.oms.model.entity.CardNetwork;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CardNetworkMapperTest {

    @InjectMocks
    private CardNetworkMapperImpl mapper = Mappers.getMapper(CardNetworkMapper.class);

    @Test
    void shouldConvertEntityToDTO_whenEntityIsProvided() {
        CardNetwork entity = new CardNetwork();
        entity.setId(1L);
        entity.setName("Visa");

        CardNetworkDTO dto = mapper.toDTO(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
    }

    @Test
    void shouldConvertDTOToEntity_whenDTOIsProvided() {
        CardNetworkDTO dto = new CardNetworkDTO();
        dto.setId(2L);
        dto.setName("MasterCard");

        CardNetwork entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
    }

    @Test
    void shouldReturnNull_whenEntityIsNull() {
        CardNetworkDTO dto = mapper.toDTO(null);
        assertEquals(null, dto);
    }

    @Test
    void shouldReturnNull_whenDTOIsNull() {
        CardNetwork entity = mapper.toEntity(null);
        assertEquals(null, entity);
    }
}