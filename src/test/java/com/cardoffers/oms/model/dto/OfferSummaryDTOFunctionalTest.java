package com.cardoffers.oms.model.dto;

import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class OfferSummaryDTOFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void shouldCreateOfferSummary() throws Exception {
        OfferSummaryDTO offerSummaryDTO = new OfferSummaryDTO();
        offerSummaryDTO.setId(1L);
        offerSummaryDTO.setTitle("Special Discount");
        offerSummaryDTO.setMerchantName("Best Merchants");
        offerSummaryDTO.setOfferType("Promotion");
        offerSummaryDTO.setDiscountPercentage(new BigDecimal("20.00"));
        offerSummaryDTO.setStartDate(LocalDate.now());
        offerSummaryDTO.setEndDate(LocalDate.now().plusDays(30));
        offerSummaryDTO.setActive(true);

        mockMvc.perform(post("/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(offerSummaryDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Special Discount"));
    }

    @Test
    public void shouldValidateOfferSummaryTitleNotBlank() throws Exception {
        OfferSummaryDTO offerSummaryDTO = new OfferSummaryDTO();
        offerSummaryDTO.setId(1L);
        offerSummaryDTO.setTitle("");
        offerSummaryDTO.setMerchantName("Test Merchant");
        offerSummaryDTO.setOfferType("Promotion");
        offerSummaryDTO.setDiscountPercentage(new BigDecimal("20.00"));
        offerSummaryDTO.setStartDate(LocalDate.now());
        offerSummaryDTO.setEndDate(LocalDate.now().plusDays(30));
        offerSummaryDTO.setActive(true);

        mockMvc.perform(post("/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(offerSummaryDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.title").value("must not be blank"));
    }

    @Test
    public void shouldReturnNotFoundForInvalidId() throws Exception {
        mockMvc.perform(get("/offers/9999"))
                .andExpect(status().isNotFound());
    }
}