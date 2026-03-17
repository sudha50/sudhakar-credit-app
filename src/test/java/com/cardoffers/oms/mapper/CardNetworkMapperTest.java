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
import com.cardoffers.oms.mapper.CardNetworkMapper;
import com.cardoffers.oms.model.dto.CardNetworkDTO;
import com.cardoffers.oms.model.entity.CardNetwork;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import static org.junit.jupiter.api.Assertions.*;

class CardNetworkMapperTest {

    private CardNetworkMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(CardNetworkMapper.class);
    }

    @Test
void shouldToDTO_mapsAllFields() {
    // Given
    CardNetwork entity = new CardNetwork();
    entity.setId(1L);
    entity.setName("Visa");
    entity.setCode("VISA");
    entity.setActive(true);
    entity.setCreatedAt(LocalDateTime.now());

    // When
    CardNetworkDTO result = mapper.toDTO(entity);

    // Then
    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals("Visa", result.getName());
    assertEquals("VISA", result.getCode());
    assertEquals(true, result.getActive());
}

    @Test
void shouldToDTO_literalNull_returnsNull() {
    // When
    CardNetworkDTO result = mapper.toDTO(null);

    // Then
    assertNull(result);
}

    @Test
void shouldToDTO_emptyObject_returnsNonNull() {
    // Given
    CardNetwork emptySource = new CardNetwork();

    // When
    CardNetworkDTO result = mapper.toDTO(emptySource);

    // Then
    assertNotNull(result);
}

    @Test
void shouldToEntity_mapsAllFields() {
    // Given
    CardNetworkDTO dto = new CardNetworkDTO();
    dto.setId(1L);
    dto.setName("Visa");
    dto.setCode("VISA");
    dto.setActive(true);

    // When
    CardNetwork result = mapper.toEntity(dto);

    // Then
    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals("Visa", result.getName());
    assertEquals("VISA", result.getCode());
    assertEquals(true, result.getActive());
}

    @Test
void shouldToEntity_literalNull_returnsNull() {
    // When
    CardNetwork result = mapper.toEntity(null);

    // Then
    assertNull(result);
}

    @Test
void shouldToEntity_emptyObject_returnsNonNull() {
    // Given
    CardNetworkDTO dto = new CardNetworkDTO();

    // When
    CardNetwork result = mapper.toEntity(dto);

    // Then
    assertNotNull(result);
}

    @Test
void shouldToDTO_mapWithMaxValues() {
    // Given
    CardNetwork entity = new CardNetwork();
    entity.setId(Long.MAX_VALUE);
    entity.setName("MaxName");
    entity.setCode("MaxCode");
    entity.setCreatedAt(LocalDateTime.MAX);
    entity.setActive(true);

    // When
    CardNetworkDTO result = mapper.toDTO(entity);

    // Then
    assertNotNull(result);
    assertEquals(Long.MAX_VALUE, result.getId());
    assertEquals("MaxName", result.getName());
    assertEquals("MaxCode", result.getCode());
    assertEquals(true, result.getActive());
}

    @Test
void shouldToEntity_mapWithMaxValues() {
    // Given
    CardNetworkDTO dto = new CardNetworkDTO();
    dto.setId(Long.MAX_VALUE);
    dto.setName("Maximum Name");
    dto.setCode("MAX_CODE");
    dto.setActive(true);

    // When
    CardNetwork result = mapper.toEntity(dto);

    // Then
    assertNotNull(result);
    assertEquals(Long.MAX_VALUE, result.getId());
    assertEquals("Maximum Name", result.getName());
    assertEquals("MAX_CODE", result.getCode());
    assertNotNull(result.getActive());
    assertTrue(result.getActive());
}

}