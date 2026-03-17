package com.cardoffers.oms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import com.cardoffers.oms.model.dto.MerchantDTO;
import com.cardoffers.oms.service.MerchantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.cache.type=none",
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration,org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration"
    })
class MerchantServiceIntegrationTest {

    @Autowired
    private MerchantService merchantService;

    @Test
void shouldCreateMerchant_happyPath() {
    // Given
    MerchantDTO dto = new MerchantDTO();
    dto.setName("Test Merchant");
    dto.setCategory("Retail");
    dto.setDescription("A merchant for testing purposes");
    dto.setLogoUrl("http://example.com/logo.png");
    dto.setWebsite("http://example.com");
    
    // When
    MerchantDTO result = merchantService.createMerchant(dto);

    // Then
    assertNotNull(result);
    assertEquals("Test Merchant", result.getName());
    assertEquals("Retail", result.getCategory());
    assertEquals("A merchant for testing purposes", result.getDescription());
    assertEquals("http://example.com/logo.png", result.getLogoUrl());
    assertEquals("http://example.com", result.getWebsite());
}

    @Test
void shouldUpdateMerchant_happyPath() {
    // Given
    MerchantDTO existingMerchant = new MerchantDTO();
    existingMerchant.setId(1L);
    existingMerchant.setName("Existing Merchant");
    existingMerchant.setCategory("Retail");
    existingMerchant.setDescription("Description");
    existingMerchant.setLogoUrl("http://logo.url");
    existingMerchant.setWebsite("http://merchant.website");
    existingMerchant.setActive(true);

    merchantRepository.save(existingMerchant); // Persisting the existing merchant

    MerchantDTO updatedMerchant = new MerchantDTO();
    updatedMerchant.setId(1L);
    updatedMerchant.setName("Updated Merchant");
    updatedMerchant.setCategory("Retail");
    updatedMerchant.setDescription("Updated Description");
    updatedMerchant.setLogoUrl("http://updated.logo.url");
    updatedMerchant.setWebsite("http://updated.merchant.website");
    updatedMerchant.setActive(true);

    // When
    MerchantDTO result = merchantService.updateMerchant(existingMerchant.getId(), updatedMerchant);

    // Then
    assertNotNull(result);
    assertEquals("Updated Merchant", result.getName());
    assertEquals("Updated Description", result.getDescription());
    assertEquals("Retail", result.getCategory());
    assertEquals("http://updated.logo.url", result.getLogoUrl());
    assertEquals("http://updated.merchant.website", result.getWebsite());
    assertNotNull(result.getActive());
    assertTrue(result.getActive());
}



    @Test
void shouldGetAllActiveMerchants_emptyList() {
    // Given
    // No active merchants in the database

    // When
    List<MerchantDTO> result = merchantService.getAllActiveMerchants();

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
}

    @Test
void shouldGetMerchantsByCategory_emptyCategory() {
    // Given
    String category = "";

    // When
    List<MerchantDTO> result = merchantService.getMerchantsByCategory(category);

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
}



}