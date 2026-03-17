package com.cardoffers.oms.service;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.springframework.test.annotation.DirtiesContext;
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

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.cache.type=none",
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration,org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration"
    })
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
    MerchantDTO merchantDTO = new MerchantDTO();
    merchantDTO.setName("Test Merchant");
    merchantDTO.setCategory("Retail");
    merchantDTO.setDescription("This is a test merchant.");
    
    Merchant merchantEntity = new Merchant();
    merchantEntity.setName("Test Merchant");
    merchantEntity.setCategory("Retail");

    when(merchantMapper.toEntity(any(MerchantDTO.class))).thenReturn(merchantEntity);
    when(merchantRepository.save(any(Merchant.class))).thenReturn(merchantEntity);
    when(merchantMapper.toDTO(any(Merchant.class))).thenReturn(merchantDTO);

    // When
    MerchantDTO result = merchantService.createMerchant(merchantDTO);

    // Then
    assertNotNull(result);
    assertEquals("Test Merchant", result.getName());
    assertEquals("Retail", result.getCategory());
}

    @Test
void shouldCreateMerchant_notFound() {
    // Given
    MerchantDTO dto = new MerchantDTO();
    dto.setName("Test Merchant");
    when(merchantRepository.findByNameIgnoreCase(any())).thenReturn(Optional.empty());
    when(merchantMapper.toEntity(any())).thenReturn(new Merchant());

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> merchantService.createMerchant(dto));
}

    @Test
void shouldUpdateMerchant_happyPath() {
    // Given
    Merchant existingMerchant = new Merchant();
    existingMerchant.setId(1L);
    existingMerchant.setName("Test Merchant");
    existingMerchant.setCategory("Retail");
    
    Merchant updatedMerchant = new Merchant();
    updatedMerchant.setId(1L);
    updatedMerchant.setName("Updated Merchant");
    updatedMerchant.setCategory("Retail");

    MerchantDTO updatedMerchantDTO = new MerchantDTO();
    updatedMerchantDTO.setId(1L);
    updatedMerchantDTO.setName("Updated Merchant");
    updatedMerchantDTO.setCategory("Retail");

    when(merchantRepository.findById(1L)).thenReturn(Optional.of(existingMerchant));
    when(merchantRepository.save(any(Merchant.class))).thenReturn(updatedMerchant);
    when(merchantMapper.toDTO(any(Merchant.class))).thenReturn(updatedMerchantDTO);

    // When
    MerchantDTO result = merchantService.updateMerchant(1L, updatedMerchantDTO);

    // Then
    assertNotNull(result);
    assertEquals("Updated Merchant", result.getName());
    assertEquals("Retail", result.getCategory());
}

    @Test
void shouldUpdateMerchant_notFound() {
    // Given
    Long merchantId = 1L;
    MerchantDTO dto = new MerchantDTO();
    dto.setName("Updated Merchant");
    
    when(merchantRepository.findById(anyLong())).thenReturn(Optional.empty());

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> merchantService.updateMerchant(merchantId, dto));
}



    @Test
void shouldGetMerchantById_nonExistentId() {
    // Given
    Long nonExistentId = 999L;
    when(merchantRepository.findById(nonExistentId)).thenReturn(Optional.empty());

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> merchantService.getMerchantById(nonExistentId));
    verify(merchantRepository).findById(nonExistentId);
}

}