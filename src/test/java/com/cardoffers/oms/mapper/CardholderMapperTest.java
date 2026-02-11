package com.cardoffers.oms.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cardoffers.oms.model.dto.CardholderDTO;
import com.cardoffers.oms.model.entity.Cardholder;

@ExtendWith(MockitoExtension.class)
class CardholderMapperTest {

    @InjectMocks
    private CardholderMapper cardholderMapper = Mappers.getMapper(CardholderMapper.class);

    @Test
    void shouldConvertEntityToDTO_whenEntityIsValid() {
        Cardholder cardholder = new Cardholder();
        cardholder.setId(1L);
        cardholder.setName("John Doe");

        CardholderDTO cardholderDTO = cardholderMapper.toDTO(cardholder);

        assertNotNull(cardholderDTO);
        assertEquals(1L, cardholderDTO.getId());
        assertEquals("John Doe", cardholderDTO.getName());
    }

    @Test
    void shouldConvertDTOToEntity_whenDTOIsValid() {
        CardholderDTO cardholderDTO = new CardholderDTO();
        cardholderDTO.setId(2L);
        cardholderDTO.setName("Jane Doe");

        Cardholder cardholder = cardholderMapper.toEntity(cardholderDTO);

        assertNotNull(cardholder);
        assertEquals(2L, cardholder.getId());
        assertEquals("Jane Doe", cardholder.getName());
    }

    @Test
    void shouldConvertListOfEntitiesToListOfDTOs_whenEntitiesAreProvided() {
        Cardholder cardholder1 = new Cardholder();
        cardholder1.setId(3L);
        cardholder1.setName("Alice");

        Cardholder cardholder2 = new Cardholder();
        cardholder2.setId(4L);
        cardholder2.setName("Bob");

        List<Cardholder> cardholders = List.of(cardholder1, cardholder2);
        List<CardholderDTO> cardholderDTOs = cardholderMapper.toDTOs(cardholders);

        assertNotNull(cardholderDTOs);
        assertEquals(2, cardholderDTOs.size());
        assertEquals("Alice", cardholderDTOs.get(0).getName());
        assertEquals("Bob", cardholderDTOs.get(1).getName());
    }

    @Test
    void shouldReturnEmptyList_whenNoEntitiesProvided() {
        List<CardholderDTO> cardholderDTOs = cardholderMapper.toDTOs(List.of());

        assertNotNull(cardholderDTOs);
        assertEquals(0, cardholderDTOs.size());
    }
}