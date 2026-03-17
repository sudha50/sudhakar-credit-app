package com.cardoffers.oms.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
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
import com.cardoffers.oms.mapper.MerchantMapper;
import com.cardoffers.oms.model.dto.MerchantDTO;
import com.cardoffers.oms.model.entity.Merchant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import static org.junit.jupiter.api.Assertions.*;

class MerchantMapperTest {

    private MerchantMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(MerchantMapper.class);
    }

    @Test
void shouldToDTO_mapsAllFields() {
    // Given
    Merchant merchant = new Merchant();
    merchant.setId(1L);
    merchant.setName("Test Merchant");
    merchant.setDescription("A test merchant");
    merchant.setCategory("Retail");
    merchant.setLogoUrl("http://example.com/logo.png");
    merchant.setWebsite("http://example.com");
    merchant.setCreatedAt(LocalDateTime.now());
    merchant.setActive(true);

    // When
    MerchantDTO result = mapper.toDTO(merchant);

    // Then
    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals("Test Merchant", result.getName());
    assertEquals("A test merchant", result.getDescription());
    assertEquals("Retail", result.getCategory());
    assertEquals("http://example.com/logo.png", result.getLogoUrl());
    assertEquals("http://example.com", result.getWebsite());
    assertEquals(true, result.getActive());
}

    @Test
void shouldToDTO_literalNull_returnsNull() {
    // Given
    Merchant entity = null;

    // When
    MerchantDTO result = mapper.toDTO(entity);

    // Then
    assertNull(result);
}

    @Test
void shouldToDTO_emptyObject_returnsNonNull() {
    // Given
    Merchant emptyMerchant = new Merchant();

    // When
    MerchantDTO result = mapper.toDTO(emptyMerchant);

    // Then
    assertNotNull(result);
}

    @Test
void shouldToEntity_mapsAllFields() {
    // Given
    MerchantDTO dto = new MerchantDTO();
    dto.setId(1L);
    dto.setName("Store A");
    dto.setDescription("A great store");
    dto.setCategory("Retail");
    dto.setLogoUrl("http://store-a-logo.com");
    dto.setWebsite("http://store-a.com");
    dto.setActive(true);

    // When
    Merchant result = mapper.toEntity(dto);

    // Then
    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals("Store A", result.getName());
    assertEquals("A great store", result.getDescription());
    assertEquals("Retail", result.getCategory());
    assertEquals("http://store-a-logo.com", result.getLogoUrl());
    assertEquals("http://store-a.com", result.getWebsite());
    assertEquals(true, result.getActive());
}

    @Test
void shouldToEntity_literalNull_returnsNull() {
    // When
    Merchant result = mapper.toEntity(null);

    // Then
    assertNull(result);
}

    @Test
void shouldToEntity_emptyObject_returnsNonNull() {
    // Given
    MerchantDTO dto = new MerchantDTO();
    
    // When
    Merchant result = mapper.toEntity(dto);
    
    // Then
    assertNotNull(result);
}

    @Test
void shouldToDTO_maxValueFields() {
    // Given
    Merchant merchant = new Merchant();
    merchant.setId(Long.MAX_VALUE);
    merchant.setName("A".repeat(255)); // Assuming max length for name is 255 characters
    merchant.setDescription(null);
    merchant.setCategory("C".repeat(255)); // Assuming max length for category is 255 characters
    merchant.setLogoUrl(null);
    merchant.setWebsite(null);
    merchant.setCreatedAt(LocalDateTime.now());
    merchant.setActive(true);

    // When
    MerchantDTO result = mapper.toDTO(merchant);

    // Then
    assertNotNull(result);
    assertEquals(Long.MAX_VALUE, result.getId());
    assertEquals("A".repeat(255), result.getName());
    assertEquals(null, result.getDescription());
    assertEquals("C".repeat(255), result.getCategory());
    assertEquals(null, result.getLogoUrl());
    assertEquals(null, result.getWebsite());
    assertEquals(true, result.getActive());
}

    @Test
void shouldToEntity_maxValueFields() {
    // Given
    MerchantDTO dto = new MerchantDTO();
    dto.setId(Long.MAX_VALUE);
    dto.setName("A".repeat(255)); // Assuming max length for name is 255
    dto.setDescription("D".repeat(255)); // Assuming max length for description is 255
    dto.setCategory("C".repeat(255)); // Assuming max length for category is 255
    dto.setLogoUrl("http://example.com/logo.png"); // Using a reasonable URL
    dto.setWebsite("http://example.com"); // Using a reasonable URL
    dto.setActive(true);

    // When
    Merchant result = mapper.toEntity(dto);

    // Then
    assertNotNull(result);
    assertEquals(Long.MAX_VALUE, result.getId());
    assertEquals("A".repeat(255), result.getName());
    assertEquals("D".repeat(255), result.getDescription());
    assertEquals("C".repeat(255), result.getCategory());
    assertEquals("http://example.com/logo.png", result.getLogoUrl());
    assertEquals("http://example.com", result.getWebsite());
    assertNotNull(result.getActive());
    assertTrue(result.getActive());
}

}