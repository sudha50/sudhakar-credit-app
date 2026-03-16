package com.cardoffers.oms.service;

import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.transaction.annotation.Transactional;
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

@SpringBootTest(exclude = {RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class, CacheAutoConfiguration.class})
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.cache.type=none"})
@Transactional
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
    merchantDTO.setName("Merchant Name");
    merchantDTO.setCategory("Retail");
    merchantDTO.setDescription("Description of merchant");
    merchantDTO.setLogoUrl("http://logo.url");
    merchantDTO.setWebsite("http://merchant.website");
    merchantDTO.setActive(true);
    
    Merchant merchantEntity = new Merchant();
    merchantEntity.setId(1L);
    merchantEntity.setName("Merchant Name");
    merchantEntity.setCategory("Retail");
    merchantEntity.setDescription("Description of merchant");
    merchantEntity.setLogoUrl("http://logo.url");
    merchantEntity.setWebsite("http://merchant.website");
    merchantEntity.setActive(true);

    
    // When
    MerchantDTO result = merchantService.createMerchant(merchantDTO);
    
    // Then
    assertNotNull(result);
    assertEquals("Merchant Name", result.getName());
    assertEquals("Retail", result.getCategory());
    assertNotNull(result.getActive());
    assertTrue(result.getActive());
}

    @Test
void shouldCreateMerchant_notFound() {
    // Given
    MerchantDTO merchantDTO = new MerchantDTO();
    merchantDTO.setName("NonExistentMerchant");
    
    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> merchantService.createMerchant(merchantDTO));
}

    @Test
void shouldUpdateMerchant_happyPath() {
    // Given
    Merchant existingMerchant = this.merchant; // already persisted in @BeforeEach
    Long id = existingMerchant.getId();
    
    MerchantDTO updateDto = new MerchantDTO();
    updateDto.setName("Updated Name");
    updateDto.setCategory("Updated Category");

    Merchant updatedMerchant = new Merchant();
    updatedMerchant.setId(id);
    updatedMerchant.setName(updateDto.getName());
    updatedMerchant.setCategory(updateDto.getCategory());
    

    // When
    MerchantDTO result = merchantService.updateMerchant(id, updateDto);

    // Then
    assertNotNull(result);
    assertEquals("Updated Name", result.getName());
    assertEquals("Updated Category", result.getCategory());
}

    @Test
void shouldUpdateMerchant_notFound() {
    // Given
    Long nonExistentId = 1L;
    MerchantDTO merchantDTO = new MerchantDTO();
    merchantDTO.setName("Updated Merchant");
    

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> {
        // CRITICAL: Read-Modify-Write pattern required.
        // Save the complete entity FIRST (all @NotNull fields), then call update.
        // The service should load the existing entity from DB before updating to preserve createdAt.
        // Pattern: EntityType saved = repository.save(new EntityType(...all fields...));
        //           UpdateDTO dto = new UpdateDTO(saved.getId(), "new value");
        //           service.update(dto); // createdAt preserved via DB read
        merchantService.updateMerchant(nonExistentId, merchantDTO);
    });
}

    @Test
void shouldGetMerchantById_nullId() {
    // Given
    Long id = null;

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> merchantService.getMerchantById(id));
}

    @Test
void shouldGetMerchantById_nonExistentId() {
    // Given
    Long nonExistentId = 999L; // Assume this ID does not exist in the database

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> merchantService.getMerchantById(nonExistentId));
}

}