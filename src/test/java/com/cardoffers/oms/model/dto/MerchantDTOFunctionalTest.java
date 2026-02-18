package com.cardoffers.oms.model.dto;

import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(MerchantDTO.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MerchantDTOFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
    }

    @Test
    void shouldCreateMerchantSuccessfully() throws Exception {
        MerchantDTO merchantDTO = new MerchantDTO();
        merchantDTO.setName("Merchant Name");
        merchantDTO.setCategory("Retail");
        merchantDTO.setDescription("Description here");
        merchantDTO.setLogoUrl("http://example.com/logo.png");
        merchantDTO.setWebsite("http://example.com");
        merchantDTO.setActive(true);

        mockMvc.perform(post("/merchants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(merchantDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Merchant Name"));
    }

    @Test
    void shouldRejectMerchantCreationWithMissingName() throws Exception {
        MerchantDTO merchantDTO = new MerchantDTO();
        merchantDTO.setCategory("Retail");
        mockMvc.perform(post("/merchants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(merchantDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").exists());
    }

    @Test
    void shouldRejectMerchantCreationWithMissingCategory() throws Exception {
        MerchantDTO merchantDTO = new MerchantDTO();
        merchantDTO.setName("Merchant Name");
        mockMvc.perform(post("/merchants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(merchantDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.category").exists());
    }
}