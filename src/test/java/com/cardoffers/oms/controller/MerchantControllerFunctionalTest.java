package com.cardoffers.oms.controller;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.annotation.Validated;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.cardoffers.oms.exception.ResourceNotFoundException;

@SpringBootTest(exclude = {RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class, CacheAutoConfiguration.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.cache.type=none"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class MerchantControllerFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MerchantService merchantService;

    @Test
void shouldGetMerchantById_returns200() throws Exception {
    // Given
    MerchantDTO merchantDTO = new MerchantDTO();
    merchantDTO.setId(1L);
    merchantDTO.setName("Test Merchant");
    merchantDTO.setCategory("Test Category");
    when(merchantService.getMerchantById(1L)).thenReturn(merchantDTO);

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
        .andExpect(jsonPath("$.category").value("Test Category"));

    // Then
    verify(merchantService).getMerchantById(1L);
}

    @Test
void shouldGetMerchantById_returns404_notFound() throws Exception {
    // Given
    when(merchantService.getMerchantById(anyLong())).thenThrow(new ResourceNotFoundException("Merchant not found"));

    // When
    mockMvc.perform(get("/api/v1/merchants/{id}", 999L))
            .andExpect(status().isNotFound());
}

    @Test
void shouldGetAllMerchants_returns200() throws Exception {
    // Given
    List<MerchantDTO> merchants = new ArrayList<>();
    MerchantDTO merchant1 = new MerchantDTO();
    merchant1.setName("Merchant A");
    merchant1.setCategory("Category 1");
    merchants.add(merchant1);
    when(merchantService.getAllMerchants()).thenReturn(merchants);

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
           .andExpect(jsonPath("$[0].name").value("Merchant A"))
           .andExpect(jsonPath("$[0].category").value("Category 1"));
}

    @Test
void shouldGetMerchantsByCategory_returns200() throws Exception {
    // Given
    String category = "Restaurants";
    MerchantDTO merchant = new MerchantDTO();
    merchant.setName("Pizza Place");
    merchant.setCategory(category);
    when(merchantService.getMerchantsByCategory(category)).thenReturn(List.of(merchant));

    // When
    mockMvc.perform(get("/api/v1/merchants/category/{category}", category))
        // S-28: GUARANTEED FAILURE — no entity persisted (no .save() found).
        // @BeforeEach MUST save the entity with ALL required fields before
        // any andExpect(status().isOk()) assertion. Without a persisted entity,
        // the service returns 404 (findById().orElseThrow() fails).
        // FIX: Add S-27 deep entity seeding to @BeforeEach:
        //   Entity saved = entityRepository.save(new Entity(...all required fields...));
        // Then reference saved.getId() in the request URL.
           .andExpect(status().isOk()) 
           .andExpect(jsonPath("$[0].name").value("Pizza Place"))
           .andExpect(jsonPath("$[0].category").value(category));

    // Then
    verify(merchantService).getMerchantsByCategory(category);
}

    @Test
void shouldGetMerchantsByCategory_returns200_emptyList() throws Exception {
    // Given
    when(merchantService.getMerchantsByCategory("non-existing-category")).thenReturn(Collections.emptyList());

    // When
    mockMvc.perform(get("/api/v1/merchants/category/{category}", "non-existing-category"))
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

    verify(merchantService).getMerchantsByCategory("non-existing-category");
}

    @Test
void shouldCreateMerchant_returns200() throws Exception {
    // Given
    MerchantDTO merchantDTO = new MerchantDTO();
    merchantDTO.setName("New Merchant");
    merchantDTO.setCategory("Retail");
    when(merchantService.createMerchant(any(MerchantDTO.class))).thenReturn(merchantDTO);

    // When
    mockMvc.perform(post("/api/v1/merchants")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\":\"New Merchant\",\"category\":\"Retail\"}"))
        // S-28: GUARANTEED FAILURE — no entity persisted (no .save() found).
        // @BeforeEach MUST save the entity with ALL required fields before
        // any andExpect(status().isOk()) assertion. Without a persisted entity,
        // the service returns 404 (findById().orElseThrow() fails).
        // FIX: Add S-27 deep entity seeding to @BeforeEach:
        //   Entity saved = entityRepository.save(new Entity(...all required fields...));
        // Then reference saved.getId() in the request URL.
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("New Merchant"))
            .andExpect(jsonPath("$.category").value("Retail"));

    // Then
    verify(merchantService).createMerchant(any(MerchantDTO.class));
}

}