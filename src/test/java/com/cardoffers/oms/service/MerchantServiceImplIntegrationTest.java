package com.cardoffers.oms.service;

import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import com.cardoffers.oms.exception.ResourceNotFoundException;
import com.cardoffers.oms.mapper.MerchantMapper;
import com.cardoffers.oms.model.dto.MerchantDTO;
import com.cardoffers.oms.model.entity.Merchant;
import com.cardoffers.oms.repository.MerchantRepository;
import com.cardoffers.oms.service.MerchantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.cache.type=none",
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration,org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration"
    })
class MerchantServiceImplIntegrationTest {

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private MerchantService merchantService;

    @BeforeEach
    void setUp() {
        // P16-B2 / S-27: Topological save strategy — parents MUST be saved before children
        // Save order: Merchant
        // CRITICAL: this.merchant is a class-level instance field.
        // Test methods MUST use this.merchant — do NOT create a new Merchant() inline.

        Merchant merchant = new Merchant();
        merchant.setName("testName");
        merchant.setCategory("testCategory");
        merchant.setCreatedAt(java.time.LocalDateTime.now());
        this.merchant = merchantRepository.save(merchant);
    }

    @Test
void shouldCreateMerchant_happyPath() {
    // Given
    MerchantDTO merchantDTO = new MerchantDTO();
    merchantDTO.setName("Test Merchant");
    merchantDTO.setCategory("Retail");
    merchantDTO.setDescription("A test merchant for integration testing.");
    merchantDTO.setLogoUrl("http://example.com/logo.png");
    merchantDTO.setWebsite("http://example.com");
    
    Merchant merchantEntity = new Merchant();
    merchantEntity.setId(1L);
    merchantEntity.setName("Test Merchant");
    merchantEntity.setCategory("Retail");
    merchantEntity.setDescription("A test merchant for integration testing.");
    merchantEntity.setLogoUrl("http://example.com/logo.png");
    merchantEntity.setWebsite("http://example.com");
    merchantEntity.setActive(true);


    // When
    MerchantDTO result = merchantService.createMerchant(merchantDTO);

    // Then
    assertNotNull(result);
    assertEquals("Test Merchant", result.getName());
    assertEquals("Retail", result.getCategory());
    assertEquals("A test merchant for integration testing.", result.getDescription());
    assertEquals("http://example.com/logo.png", result.getLogoUrl());
    assertEquals("http://example.com", result.getWebsite());
}

    @Test
void shouldCreateMerchant_notFound() {
    // Given
    Long nonExistentId = 1L;
    MerchantDTO merchantDTO = new MerchantDTO();
    merchantDTO.setId(nonExistentId);
    merchantDTO.setName("Test Merchant");

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> merchantService.createMerchant(merchantDTO));
}

    @Test
void shouldUpdateMerchant_happyPath() {
    // Given
    Merchant existingMerchant = new Merchant();
    existingMerchant.setId(1L);
    existingMerchant.setName("Merchant A");
    existingMerchant.setCategory("Retail");
    existingMerchant.setActive(true);
    existingMerchant.setCreatedAt(java.time.LocalDateTime.now());
    merchantRepository.save(existingMerchant);

    MerchantDTO updatedDto = new MerchantDTO();
    updatedDto.setName("Updated Merchant A");
    updatedDto.setCategory("Retail");


    // When
    MerchantDTO result = merchantService.updateMerchant(1L, updatedDto);

    // Then
    assertNotNull(result);
    assertEquals("Updated Merchant A", result.getName());
    assertEquals("Retail", result.getCategory());
}

    @Test
void shouldUpdateMerchant_notFound() {
    // Given
    Long nonExistentId = 1L;
    MerchantDTO merchantDTO = new MerchantDTO();
    merchantDTO.setName("Updated Merchant");

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> merchantService.updateMerchant(nonExistentId, merchantDTO));
}

    @Test
void shouldGetMerchantById_nullId() {
    // Given
    Long id = null;

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> merchantService.getMerchantById(id));
}

    @Test
void shouldGetMerchantById_nonExistingId() {
    // Given
    Long nonExistingId = 999L;

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> merchantService.getMerchantById(nonExistingId));
}

}