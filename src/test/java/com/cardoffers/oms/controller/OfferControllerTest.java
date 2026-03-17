package com.cardoffers.oms.controller;

import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.anyString;
import org.springframework.test.context.ActiveProfiles;
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
import com.cardoffers.oms.controller.OfferController;
import com.cardoffers.oms.model.dto.OfferDTO;
import com.cardoffers.oms.model.dto.OfferSummaryDTO;
import com.cardoffers.oms.service.OfferService;
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
import com.cardoffers.oms.exception.ResourceNotFoundException;

@WebMvcTest(OfferController.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
class OfferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OfferService offerService;

    @Test
void shouldGetAllActiveOffers_returns200() throws Exception {
    // Given
    List<OfferSummaryDTO> offers = new ArrayList<>();
    OfferSummaryDTO offer = new OfferSummaryDTO();
    offer.setId(1L);
    offer.setTitle("Summer Sale");
    offers.add(offer);
    when(offerService.getAllActiveOffers()).thenReturn(offers);

    // When
    mockMvc.perform(get("/api/v1/offers"))
        // Then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].title").value("Summer Sale"));

    verify(offerService).getAllActiveOffers();
}

    @Test
void shouldGetOfferById_returns200() throws Exception {
    // Given
    OfferDTO offerDTO = new OfferDTO();
    offerDTO.setId(1L);
    offerDTO.setTitle("Special Offer");
    offerDTO.setOfferType("Discount");
    offerDTO.setStartDate(LocalDate.now());
    offerDTO.setEndDate(LocalDate.now().plusDays(10));
    offerDTO.setSource("Online");
    // Set other required fields if necessary...

    when(offerService.getOfferById(1L)).thenReturn(offerDTO);

    // When
    mockMvc.perform(get("/api/v1/offers/{id}", 1L))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(1L))
           .andExpect(jsonPath("$.title").value("Special Offer"))
           .andExpect(jsonPath("$.offerType").value("Discount"));

    // Then
    verify(offerService).getOfferById(1L);
}

    @Test
void shouldGetOfferById_returns404_notFound() throws Exception {
    // Given
    Long nonExistentId = 999L;
    when(offerService.getOfferById(nonExistentId)).thenThrow(new ResourceNotFoundException("Not found"));

    // When & Then
    mockMvc.perform(get("/api/v1/offers/{id}", nonExistentId))
           .andExpect(status().isNotFound());
}

    @Test
void shouldGetOffersByMerchant_returns200() throws Exception {
    // Given
    Long merchantId = 1L;
    OfferSummaryDTO offerSummary = new OfferSummaryDTO();
    offerSummary.setId(1L);
    offerSummary.setTitle("Special Offer");
    offerSummary.setMerchantName("Merchant A");
    offerSummary.setOfferType("Discount");
    offerSummary.setDiscountPercentage(BigDecimal.valueOf(20));
    offerSummary.setStartDate(LocalDate.now());
    offerSummary.setEndDate(LocalDate.now().plusDays(10));
    offerSummary.setActive(true);
    List<OfferSummaryDTO> offerList = Collections.singletonList(offerSummary);
    when(offerService.getOffersByMerchant(merchantId)).thenReturn(offerList);

    // When
    mockMvc.perform(get("/api/v1/offers/merchant/{merchantId}", merchantId))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].id").value(1L))
           .andExpect(jsonPath("$[0].title").value("Special Offer"))
           .andExpect(jsonPath("$[0].merchantName").value("Merchant A"))
           .andExpect(jsonPath("$[0].offerType").value("Discount"))
           .andExpect(jsonPath("$[0].discountPercentage").value(20))
           .andExpect(jsonPath("$[0].startDate").value(LocalDate.now().toString()))
           .andExpect(jsonPath("$[0].endDate").value(LocalDate.now().plusDays(10).toString()))
           .andExpect(jsonPath("$[0].active").value(true));

    // Then
    verify(offerService).getOffersByMerchant(merchantId);
}

    @Test
void shouldGetOffersByMerchant_returns200_emptyList() throws Exception {
    // Given
    Long merchantId = 1L;
    when(offerService.getOffersByMerchant(merchantId)).thenReturn(Collections.emptyList());

    // When
    mockMvc.perform(get("/api/v1/offers/merchant/{merchantId}", merchantId))
        // Then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isEmpty());

    verify(offerService).getOffersByMerchant(merchantId);
}

    @Test
void shouldGetOffersByNetwork_returns200() throws Exception {
    // Given
    List<OfferSummaryDTO> offers = new ArrayList<>();
    OfferSummaryDTO offer = new OfferSummaryDTO();
    offer.setId(1L);
    offer.setTitle("Special Offer");
    offers.add(offer);
    when(offerService.getOffersByNetwork(1L)).thenReturn(offers);

    // When
    mockMvc.perform(get("/api/v1/offers/network/{networkId}", 1L))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].id").value(1L))
           .andExpect(jsonPath("$[0].title").value("Special Offer"));

    // Then
    verify(offerService).getOffersByNetwork(1L);
}

    @Test
void shouldGetOffersByNetwork_returns200_emptyList() throws Exception {
    // Given
    Long networkId = 1L;
    when(offerService.getOffersByNetwork(networkId)).thenReturn(new ArrayList<>());

    // When
    mockMvc.perform(get("/api/v1/offers/network/{networkId}", networkId))
        // Then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isEmpty());
}

    @Test
void shouldSearchOffers_returns200() throws Exception {
    // Given
    OfferSummaryDTO offerSummary = new OfferSummaryDTO();
    offerSummary.setId(1L);
    offerSummary.setTitle("Special Discount");
    offerSummary.setMerchantName("Merchant A");
    offerSummary.setOfferType("Discount");
    offerSummary.setDiscountPercentage(new BigDecimal("15.00"));
    offerSummary.setStartDate(LocalDate.now());
    offerSummary.setEndDate(LocalDate.now().plusDays(30));
    offerSummary.setActive(true);
    List<OfferSummaryDTO> offers = Collections.singletonList(offerSummary);
    when(offerService.searchOffers(anyString(), anyString(), anyString())).thenReturn(offers);

    // When
    mockMvc.perform(get("/api/v1/offers/search")
            .param("keyword", "Discount")
            .param("offerType", "Discount")
            .param("category", "Special"))
            // Then
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].title").value("Special Discount"))
            .andExpect(jsonPath("$[0].merchantName").value("Merchant A"))
            .andExpect(jsonPath("$[0].offerType").value("Discount"))
            .andExpect(jsonPath("$[0].discountPercentage").value(15.00));
}

}