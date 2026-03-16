package com.cardoffers.oms.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import com.cardoffers.oms.mapper.CardholderMapper;
import com.cardoffers.oms.model.dto.CardholderDTO;
import com.cardoffers.oms.model.entity.Cardholder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import static org.junit.jupiter.api.Assertions.*;

class CardholderMapperTest {

    private CardholderMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(CardholderMapper.class);
    }

    @Test
void shouldToDTO_mapsAllFields() {
    // Given
    Cardholder cardholder = new Cardholder();
    cardholder.setId(1L);
    cardholder.setFirstName("John");
    cardholder.setLastName("Doe");
    cardholder.setEmail("john.doe@example.com");
    cardholder.setPhoneNumber("123-456-7890");
    cardholder.setCreatedAt(LocalDateTime.now());
    cardholder.setUpdatedAt(LocalDateTime.now());
    cardholder.setActive(true);

    // When
    CardholderDTO result = cardholderMapper.toDTO(cardholder);

    // Then
    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals("John", result.getFirstName());
    assertEquals("Doe", result.getLastName());
    assertEquals("john.doe@example.com", result.getEmail());
    assertEquals("123-456-7890", result.getPhoneNumber());
    assertNotNull(result.getActive());
    assertTrue(result.getActive());
}

    @Test
void shouldToDTO_literalNull_returnsNull() {
    // Given
    CardholderMapper mapper = new CardholderMapper();

    // When
    CardholderDTO result = mapper.toDTO(null);

    // Then
    assertNull(result);
}

    @Test
void shouldToDTO_emptyObject_returnsNonNull() {
    // Given
    Cardholder emptyCardholder = new Cardholder();

    // When
    CardholderDTO result = cardholderMapper.toDTO(emptyCardholder);

    // Then
    assertNotNull(result);
}

    @Test
void shouldToEntity_mapsAllFields() {
    // Given
    CardholderDTO dto = new CardholderDTO();
    dto.setFirstName("John");
    dto.setLastName("Doe");
    dto.setEmail("john.doe@example.com");
    dto.setPhoneNumber("123-456-7890");
    dto.setActive(true);

    // When
    Cardholder result = cardholderMapper.toEntity(dto);

    // Then
    assertNotNull(result);
    assertEquals("John", result.getFirstName());
    assertEquals("Doe", result.getLastName());
    assertEquals("john.doe@example.com", result.getEmail());
    assertEquals("123-456-7890", result.getPhoneNumber());
    assertEquals(true, result.getActive());
}

    @Test
void shouldToEntity_literalNull_returnsNull() {
    // When
    Cardholder result = mapper.toEntity(null);

    // Then
    assertNull(result);
}

    @Test
void shouldToEntity_emptyObject_returnsNonNull() {
    // Given
    CardholderDTO emptyDto = new CardholderDTO();

    // When
    Cardholder result = cardholderMapper.toEntity(emptyDto);

    // Then
    assertNotNull(result);
}

    @Test
void shouldToDTOs_mapsAllFields() {
    // Given
    Cardholder cardholder = new Cardholder();
    cardholder.setId(1L);
    cardholder.setFirstName("John");
    cardholder.setLastName("Doe");
    cardholder.setEmail("john.doe@example.com");
    cardholder.setPhoneNumber("1234567890");
    cardholder.setCreatedAt(LocalDateTime.now());
    cardholder.setUpdatedAt(LocalDateTime.now());
    cardholder.setActive(true);

    List<Cardholder> cardholders = Collections.singletonList(cardholder);
    
    // When
    List<CardholderDTO> result = cardholderMapper.toDTOs(cardholders);
    
    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    CardholderDTO dto = result.get(0);
    assertEquals(1L, dto.getId());
    assertEquals("John", dto.getFirstName());
    assertEquals("Doe", dto.getLastName());
    assertEquals("john.doe@example.com", dto.getEmail());
    assertEquals("1234567890", dto.getPhoneNumber());
    assertNotNull(dto.getActive());
    assertTrue(dto.getActive());
}

    @Test
void shouldToDTOs_literalNull_returnsNull() {
    // When
    List<CardholderDTO> result = mapper.toDTOs(null);

    // Then
    assertNull(result);
}

}