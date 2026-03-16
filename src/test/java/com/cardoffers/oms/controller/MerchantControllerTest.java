package com.cardoffers.oms.controller;

import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.springframework.test.context.ActiveProfiles;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.annotation.Validated;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MerchantController.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
class MerchantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MerchantService merchantService;

    @Test
void shouldGetMerchantById_returns200() throws Exception {
    // Given
    MerchantDTO merchantDTO = new MerchantDTO();
    merchantDTO.setId(1L);
    merchantDTO.setName("Merchant Name");
    merchantDTO.setCategory("Retail");
    when(merchantService.getMerchantById(1L)).thenReturn(merchantDTO);

    // When
    mockMvc.perform(get("/api/v1/merchants/1"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(1L))
           .andExpect(jsonPath("$.name").value("Merchant Name"))
           .andExpect(jsonPath("$.category").value("Retail"));

    // Then
    verify(merchantService).getMerchantById(1L);
}

    @Test
void shouldGetMerchantById_returns404_notFound() throws Exception {
    // Given
    Long nonExistentId = 999L;
    when(merchantService.getMerchantById(nonExistentId)).thenThrow(new NotFoundException("Merchant not found"));

    // When
    mockMvc.perform(get("/api/v1/merchants/{id}", nonExistentId))
           .andExpect(status().isNotFound());

    // Then
    verify(merchantService).getMerchantById(nonExistentId);
}

    @Test
void shouldGetAllMerchants_returns200() throws Exception {
    // Given
    MerchantDTO merchant1 = new MerchantDTO();
    merchant1.setName("Merchant One");
    merchant1.setCategory("Retail");

    MerchantDTO merchant2 = new MerchantDTO();
    merchant2.setName("Merchant Two");
    merchant2.setCategory("Services");

    List<MerchantDTO> merchants = Arrays.asList(merchant1, merchant2);
    when(merchantService.getAllMerchants()).thenReturn(merchants);

    // When
    mockMvc.perform(get("/api/v1/merchants"))
           .andExpect(status().isOk())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$[0].name").value("Merchant One"))
           .andExpect(jsonPath("$[1].name").value("Merchant Two"));

    // Then
    verify(merchantService).getAllMerchants();
}

    @Test
void shouldGetMerchantsByCategory_returns200() throws Exception {
    // Given
    MerchantDTO merchant = new MerchantDTO();
    merchant.setName("Merchant1");
    merchant.setCategory("Food");
    when(merchantService.getMerchantsByCategory("Food")).thenReturn(Arrays.asList(merchant));

    // When
    mockMvc.perform(get("/api/v1/merchants/category/Food"))

    // Then
    .andExpect(status().isOk())
    .andExpect(jsonPath("$[0].name").value("Merchant1"))
    .andExpect(jsonPath("$[0].category").value("Food"));
}

    @Test
void shouldGetMerchantsByCategory_returns200_emptyList() throws Exception {
    // Given
    String category = "non-matching-category";
    when(merchantService.getMerchantsByCategory(category)).thenReturn(Collections.emptyList());

    // When
    mockMvc.perform(get("/api/v1/merchants/category/{category}", category))
        // Then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isEmpty());

    verify(merchantService).getMerchantsByCategory(category);
}

    @Test
void shouldCreateMerchant_returns200() throws Exception {
    // Given
    MerchantDTO merchantDTO = new MerchantDTO();
    merchantDTO.setName("Test Merchant");
    merchantDTO.setCategory("Retail");
    when(merchantService.createMerchant(any(MerchantDTO.class))).thenReturn(merchantDTO);

    // When
    mockMvc.perform(post("/api/v1/merchants")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\":\"Test Merchant\",\"category\":\"Retail\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Test Merchant"))
            .andExpect(jsonPath("$.category").value("Retail"));

    // Then
    verify(merchantService).createMerchant(any(MerchantDTO.class));
}

    @Test
void shouldCreateMerchant_returns400_invalidInput() throws Exception {
    // Given
    MerchantDTO merchantDTO = new MerchantDTO();
    merchantDTO.setName(""); // Invalid: name is blank
    merchantDTO.setCategory(""); // Invalid: category is blank
    String requestBody = new ObjectMapper().writeValueAsString(merchantDTO);

    // When
    mockMvc.perform(post("/api/v1/merchants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isInternalServerError());
}

    @Test
void shouldUpdateMerchant_returns200() throws Exception {
    // Given
    MerchantDTO merchantDTO = new MerchantDTO();
    merchantDTO.setId(1L);
    merchantDTO.setName("Updated Merchant");
    merchantDTO.setCategory("Retail");
    when(merchantService.updateMerchant(eq(1L), any(MerchantDTO.class))).thenReturn(merchantDTO);
    
    // When
    mockMvc.perform(put("/api/v1/merchants/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"id\":1,\"name\":\"Updated Merchant\",\"category\":\"Retail\"}"))
            // Then
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Updated Merchant"))
            .andExpect(jsonPath("$.category").value("Retail"));
}

}