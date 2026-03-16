package com.cardoffers.oms.service;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest(exclude = {RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class, CacheAutoConfiguration.class})
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.cache.type=none"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class MerchantServiceImplFunctionalTest {

    @MockBean
    private MerchantRepository merchantRepository;

    @MockBean
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
    MerchantDTO inputDto = new MerchantDTO();
    inputDto.setName("Test Merchant");
    inputDto.setCategory("Retail");
    inputDto.setDescription("A description of the test merchant.");
    inputDto.setLogoUrl("http://example.com/logo.png");
    inputDto.setWebsite("http://example.com");
    inputDto.setActive(true);

    Merchant merchantEntity = new Merchant();
    merchantEntity.setName("Test Merchant");
    merchantEntity.setCategory("Retail");
    merchantEntity.setDescription("A description of the test merchant.");
    merchantEntity.setLogoUrl("http://example.com/logo.png");
    merchantEntity.setWebsite("http://example.com");
    merchantEntity.setActive(true);
    
    when(merchantMapper.toEntity(inputDto)).thenReturn(merchantEntity);
    when(merchantRepository.save(any(Merchant.class))).thenReturn(merchantEntity);
    when(merchantMapper.toDTO(merchantEntity)).thenReturn(inputDto);

    // When
    MerchantDTO result = merchantService.createMerchant(inputDto);

    // Then
    assertNotNull(result);
    assertEquals("Test Merchant", result.getName());
    assertEquals("Retail", result.getCategory());
    assertEquals("A description of the test merchant.", result.getDescription());
    assertEquals("http://example.com/logo.png", result.getLogoUrl());
    assertEquals("http://example.com", result.getWebsite());
    assertNotNull(result.getActive());
    assertTrue(result.getActive());
}

    @Test
void shouldCreateMerchant_notFound() {
    // Given
    MerchantDTO merchantDTO = new MerchantDTO();
    merchantDTO.setName("Test Merchant");
    when(merchantRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());
    when(merchantMapper.toEntity(any())).thenReturn(new Merchant());

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> merchantService.createMerchant(merchantDTO));
}

    @Test
void shouldUpdateMerchant_happyPath() {
    // Given
    Long id = this.merchant.getId();
    MerchantDTO updateDTO = new MerchantDTO();
    updateDTO.setName("Updated Merchant");
    updateDTO.setCategory("Retail");
    when(merchantRepository.findById(id)).thenReturn(Optional.of(this.merchant));
    when(merchantRepository.save(any())).thenReturn(this.merchant);
    when(merchantMapper.toDTO(any())).thenReturn(updateDTO);

    // When
    MerchantDTO result = merchantService.updateMerchant(id, updateDTO);

    // Then
    assertNotNull(result);
    assertEquals("Updated Merchant", result.getName());
    assertEquals("Retail", result.getCategory());
    verify(merchantRepository).findById(id);
    verify(merchantRepository).save(any());
    verify(merchantMapper).toDTO(any());
}

    @Test
void shouldUpdateMerchant_notFound() {
    // Given
    Long nonExistentId = 1L;
    MerchantDTO merchantDTO = new MerchantDTO();
    merchantDTO.setName("Test Merchant");
    when(merchantRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());
    when(merchantMapper.toEntity(any(MerchantDTO.class))).thenReturn(new Merchant());

    // When & Then
    assertThrows(ResourceNotFoundException.class, () ->        // CRITICAL: Read-Modify-Write pattern required.
        // Save the complete entity FIRST (all @NotNull fields), then call update.
        // The service should load the existing entity from DB before updating to preserve createdAt.
        // Pattern: EntityType saved = repository.save(new EntityType(...all fields...));
        //           UpdateDTO dto = new UpdateDTO(saved.getId(), "new value");
        //           service.update(dto); // createdAt preserved via DB read
 merchantService.updateMerchant(nonExistentId, merchantDTO));
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
    Long nonExistentId = 999L;
    when(merchantRepository.findByNameIgnoreCase("NonExistentMerchant")).thenReturn(Optional.empty());

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> merchantService.getMerchantById(nonExistentId));
}

}