package com.cardoffers.oms.controller;

import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import com.cardoffers.oms.model.dto.EligibleOfferResponseDTO;
import com.cardoffers.oms.model.dto.OfferDTO;
import com.cardoffers.oms.service.OfferEligibilityService;

@WebMvcTest(OfferEligibilityController.class)
@ActiveProfiles("test")
class OfferEligibilityControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OfferEligibilityService offerEligibilityService;

    private static EligibleOfferResponseDTO anEligibleOfferResponseDTO() {
        EligibleOfferResponseDTO entity = new EligibleOfferResponseDTO();
        return entity;
    }

    private static OfferDTO anOfferDTO() {
        OfferDTO entity = new OfferDTO();
        entity.setTitle("Test Title");
        entity.setDescription("Test description");
        entity.setOfferType("DEFAULT");
        entity.setDiscountPercentage(1);
        entity.setCashbackAmount(new java.math.BigDecimal("100.00"));
        entity.setMinimumPurchaseAmount(new java.math.BigDecimal("100.00"));
        entity.setStartDate(java.time.LocalDate.of(2025, 1, 15));
        entity.setEndDate(java.time.LocalDate.of(2025, 1, 15));
        entity.setTermsAndConditions("test-value");
        entity.setMerchant(null); // TODO: set MerchantDTO
        entity.setCardNetwork(null); // TODO: set CardNetworkDTO
        entity.setSource("test-value");
        entity.setMaxRedemptions(1);
        entity.setCurrentRedemptions(1);
        entity.setActive(true);
        return entity;
    }

    @BeforeEach
    void setUp() {
        // Setup common mocks if required
    }

    @Test
    void shouldReturnEligibleOffers_whenValidCardholderIdProvided() throws Exception {
        Long cardholderId = 1L;
        EligibleOfferResponseDTO responseDTO = anEligibleOfferResponseDTO();
        responseDTO.setCardholderId(cardholderId);

        Mockito.when(offerEligibilityService.getEligibleOffersForCardholder(cardholderId)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/eligible-offers/cardholder/{cardholderId}", cardholderId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cardholderId").value(cardholderId));
    }

    @Test
    void shouldReturnOffersByCategory_whenValidCardholderIdAndCategoryProvided() throws Exception {
        Long cardholderId = 1L;
        String category = "TestCategory";
        List<OfferDTO> offers = Collections.singletonList(anOfferDTO());

        Mockito.when(offerEligibilityService.filterOffersByCategory(cardholderId, category)).thenReturn(offers);

        mockMvc.perform(get("/api/v1/eligible-offers/cardholder/{cardholderId}/category/{category}", cardholderId, category)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("Test Title"));
    }

    @Test
    void shouldReturnOffersByType_whenValidCardholderIdAndOfferTypeProvided() throws Exception {
        Long cardholderId = 1L;
        String offerType = "TestType";
        List<OfferDTO> offers = Collections.singletonList(anOfferDTO());

        Mockito.when(offerEligibilityService.filterOffersByOfferType(cardholderId, offerType)).thenReturn(offers);

        mockMvc.perform(get("/api/v1/eligible-offers/cardholder/{cardholderId}/type/{offerType}", cardholderId, offerType)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].offerType").value("DEFAULT"));
    }
}