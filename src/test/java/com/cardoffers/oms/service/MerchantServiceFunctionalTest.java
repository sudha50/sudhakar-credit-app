package com.cardoffers.oms.service;

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
import com.cardoffers.oms.model.dto.MerchantDTO;
import com.cardoffers.oms.service.MerchantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.cache.type=none",
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration,org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration"
    })
class MerchantServiceFunctionalTest {

    @Autowired
    private MerchantService merchantService;

    @Test
void shouldCreateMerchant_happyPath() {
    // Given
    MerchantDTO merchantDTO = new MerchantDTO();
    merchantDTO.setName("Test Merchant");
    merchantDTO.setCategory("Retail");
    merchantDTO.setDescription("A test merchant for integration testing.");
    merchantDTO.setLogoUrl("http://example.com/logo.png");
    merchantDTO.setWebsite("http://example.com");
    merchantDTO.setActive(true);

    // When
    MerchantDTO result = merchantService.createMerchant(merchantDTO);

    // Then
    assertNotNull(result);
    assertEquals("Test Merchant", result.getName());
    assertEquals("Retail", result.getCategory());
    assertNotNull(result.getActive());
    assertTrue(result.getActive());
}

    @Test
void shouldUpdateMerchant_happyPath() {
    // Given
    MerchantDTO updatedMerchantDTO = new MerchantDTO();
    updatedMerchantDTO.setName("Updated Merchant");
    updatedMerchantDTO.setCategory("Updated Category");
    updatedMerchantDTO.setDescription("Updated description");
    Long id = this.merchant.getId();

    // When
    MerchantDTO result = merchantService.updateMerchant(id, updatedMerchantDTO);

    // Then
    assertNotNull(result);
    assertEquals("Updated Merchant", result.getName());
    assertEquals("Updated Category", result.getCategory());
}



    @Test
void shouldGetMerchantById_negativeId() {
    // Given
    Long negativeId = -1L;

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> merchantService.getMerchantById(negativeId));
}

    @Test
void shouldGetMerchantsByCategory_emptyCategory() throws Exception {
    // Given
    String category = "";

    // When
    List<MerchantDTO> result = merchantService.getMerchantsByCategory(category);

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
}



}