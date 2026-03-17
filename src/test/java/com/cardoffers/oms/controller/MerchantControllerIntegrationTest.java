package com.cardoffers.oms.controller;

import org.junit.jupiter.api.BeforeEach;
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
import com.cardoffers.oms.controller.MerchantController;
import com.cardoffers.oms.model.dto.MerchantDTO;
import com.cardoffers.oms.service.MerchantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.annotation.Validated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.cache.type=none",
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration,org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration"
    })
class MerchantControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
void shouldGetMerchantById_returns200() throws Exception {
    // Given
    MerchantDTO merchant = new MerchantDTO();
    merchant.setId(1L);
    merchant.setName("Test Merchant");
    merchant.setCategory("Retail");

    // When
    mockMvc.perform(get("/api/v1/merchants/{id}", 1L))
        // S-28: GUARANTEED FAILURE — no entity persisted (no .save() found).
        // @BeforeEach MUST save the entity with ALL required fields before
        // any andExpect(status().isOk()) assertion. Without a persisted entity,
        // the service returns 404 (findById().orElseThrow() fails).
        // FIX: Add S-27 deep entity seeding to @BeforeEach:
        //   Entity saved = entityRepository.save(new Entity(...all required fields...));
        // Then reference saved.getId() in the request URL.
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.name").value("Test Merchant"))
        .andExpect(jsonPath("$.category").value("Retail"));

    // Then
}

    @Test
void shouldGetMerchantById_returns404_notFound() throws Exception {
    // Given
    long nonExistentId = 999L;

    // When
    mockMvc.perform(get("/api/v1/merchants/{id}", nonExistentId))
            // Then
            .andExpect(status().isNotFound());
}

    @Test
void shouldGetAllMerchants_returns200() throws Exception {
    // Given
    MerchantDTO merchant1 = new MerchantDTO();
    merchant1.setId(1L);
    merchant1.setName("Merchant One");
    merchant1.setCategory("Category A");

    MerchantDTO merchant2 = new MerchantDTO();
    merchant2.setId(2L);
    merchant2.setName("Merchant Two");
    merchant2.setCategory("Category B");

    // Mock the service to return the list of merchants
    // Assuming there's a method to set the mocked behavior for the service

    // When
    mockMvc.perform(get("/api/v1/merchants"))
        // S-28: GUARANTEED FAILURE — no entity persisted (no .save() found).
        // @BeforeEach MUST save the entity with ALL required fields before
        // any andExpect(status().isOk()) assertion. Without a persisted entity,
        // the service returns 404 (findById().orElseThrow() fails).
        // FIX: Add S-27 deep entity seeding to @BeforeEach:
        //   Entity saved = entityRepository.save(new Entity(...all required fields...));
        // Then reference saved.getId() in the request URL.
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("Merchant One"))
            .andExpect(jsonPath("$[1].name").value("Merchant Two"));
}

    @Test
void shouldGetMerchantsByCategory_returns200() throws Exception {
    // Given
    MerchantDTO merchant1 = new MerchantDTO();
    merchant1.setId(1L);
    merchant1.setName("Merchant A");
    merchant1.setCategory("Electronics");
    
    MerchantDTO merchant2 = new MerchantDTO();
    merchant2.setId(2L);
    merchant2.setName("Merchant B");
    merchant2.setCategory("Electronics");

    List<MerchantDTO> merchants = Arrays.asList(merchant1, merchant2);
    
    // Assuming MerchantService has a method for this, but we don't mock here in integration tests.
    // When
    mockMvc.perform(get("/api/v1/merchants/category/{category}", "Electronics"))
        // S-28: GUARANTEED FAILURE — no entity persisted (no .save() found).
        // @BeforeEach MUST save the entity with ALL required fields before
        // any andExpect(status().isOk()) assertion. Without a persisted entity,
        // the service returns 404 (findById().orElseThrow() fails).
        // FIX: Add S-27 deep entity seeding to @BeforeEach:
        //   Entity saved = entityRepository.save(new Entity(...all required fields...));
        // Then reference saved.getId() in the request URL.
           .andExpect(status().isOk())
           .andExpect(jsonPath("$", hasSize(2)))
           .andExpect(jsonPath("$[0].name").value("Merchant A"))
           .andExpect(jsonPath("$[1].name").value("Merchant B"));
}

    @Test
void shouldGetMerchantsByCategory_returns200_emptyList() throws Exception {
    // Given
    String category = "non-matching-category";

    // When
    mockMvc.perform(get("/api/v1/merchants/category/{category}", category))
        // Then
        // S-28: GUARANTEED FAILURE — no entity persisted (no .save() found).
        // @BeforeEach MUST save the entity with ALL required fields before
        // any andExpect(status().isOk()) assertion. Without a persisted entity,
        // the service returns 404 (findById().orElseThrow() fails).
        // FIX: Add S-27 deep entity seeding to @BeforeEach:
        //   Entity saved = entityRepository.save(new Entity(...all required fields...));
        // Then reference saved.getId() in the request URL.
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isEmpty());
}

    @Test
void shouldCreateMerchant_returns200() throws Exception {
    // Given
    MerchantDTO merchantDTO = new MerchantDTO();
    merchantDTO.setName("Test Merchant");
    merchantDTO.setCategory("Retail");
    

    // When
    mockMvc.perform(post("/api/v1/merchants")
        .contentType("application/json")
        .content("{\"name\":\"Test Merchant\",\"category\":\"Retail\"}"))
        // S-28: GUARANTEED FAILURE — no entity persisted (no .save() found).
        // @BeforeEach MUST save the entity with ALL required fields before
        // any andExpect(status().isOk()) assertion. Without a persisted entity,
        // the service returns 404 (findById().orElseThrow() fails).
        // FIX: Add S-27 deep entity seeding to @BeforeEach:
        //   Entity saved = entityRepository.save(new Entity(...all required fields...));
        // Then reference saved.getId() in the request URL.
        .andExpect(status().isCreated())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.name").value("Test Merchant"))
        .andExpect(jsonPath("$.category").value("Retail"));
}

}