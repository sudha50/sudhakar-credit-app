package com.cardoffers.oms.controller;

import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import static org.hamcrest.Matchers.hasSize;

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
    OfferSummaryDTO offer1 = new OfferSummaryDTO();
    offer1.setId(1L);
    offer1.setTitle("Discount Offer");
    offer1.setActive(true);
    
    OfferSummaryDTO offer2 = new OfferSummaryDTO();
    offer2.setId(2L);
    offer2.setTitle("Cashback Offer");
    offer2.setActive(true);
    
    List<OfferSummaryDTO> offers = Arrays.asList(offer1, offer2);
    when(offerService.getAllActiveOffers()).thenReturn(offers);

    // When
    mockMvc.perform(get("/api/v1/offers"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$", hasSize(2)))
           .andExpect(jsonPath("$[0].title", is("Discount Offer")))
           .andExpect(jsonPath("$[1].title", is("Cashback Offer")));

    // Then
    verify(offerService).getAllActiveOffers();
}

    @Test
void shouldGetOfferById_returns200() throws Exception {
    // Given
    OfferDTO offer = new OfferDTO();
    offer.setId(1L);
    offer.setTitle("Special Offer");
    offer.setOfferType("Discount");
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(10));
    offer.setSource("Website");
    when(offerService.getOfferById(1L)).thenReturn(offer);

    // When
    mockMvc.perform(get("/api/v1/offers/1"))
        // Then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.title").value("Special Offer"))
        .andExpect(jsonPath("$.offerType").value("Discount"));
}

    @Test
void shouldGetOfferById_returns404_notFound() throws Exception {
    // Given
    Long nonExistentId = 999L;
    when(offerService.getOfferById(nonExistentId)).thenThrow(new ResourceNotFoundException("Offer not found"));

    // When
    mockMvc.perform(get("/api/v1/offers/{id}", nonExistentId))
           .andExpect(status().isNotFound());

    // Then
    verify(offerService).getOfferById(nonExistentId);
}

    @Test
void shouldGetOffersByMerchant_returns200() throws Exception {
    // Given
    List<OfferSummaryDTO> offers = new ArrayList<>();
    OfferSummaryDTO offer = new OfferSummaryDTO();
    offer.setId(1L);
    offer.setTitle("Special Offer");
    offer.setMerchantName("Merchant A");
    offer.setOfferType("Discount");
    offer.setDiscountPercentage(BigDecimal.valueOf(10));
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(30));
    offer.setActive(true);
    offers.add(offer);
    when(offerService.getOffersByMerchant(1L)).thenReturn(offers);

    // When
    mockMvc.perform(get("/api/v1/offers/merchant/{merchantId}", 1L))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].id").value(1L))
           .andExpect(jsonPath("$[0].title").value("Special Offer"))
           .andExpect(jsonPath("$[0].merchantName").value("Merchant A"))
           .andExpect(jsonPath("$[0].offerType").value("Discount"))
           .andExpect(jsonPath("$[0].discountPercentage").value(10));
}

    @Test
void shouldGetOffersByMerchant_returns200_emptyList() throws Exception {
    // Given
    when(offerService.getOffersByMerchant(anyLong())).thenReturn(Collections.emptyList());

    // When & Then
    mockMvc.perform(get("/api/v1/offers/merchant/{merchantId}", 999L))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$").isEmpty());
}

    @Test
void shouldGetOffersByNetwork_returns200() throws Exception {
    // Given
    Long networkId = 1L;
    OfferDTO offer = new OfferDTO();
    offer.setTitle("Special Offer");
    offer.setOfferType("Discount");
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(7));
    offer.setSource("Web");
    List<OfferDTO> offers = Collections.singletonList(offer);
    when(offerService.getOffersByNetwork(networkId)).thenReturn(offers);

    // When
    mockMvc.perform(get("/api/v1/offers/network/{networkId}", networkId))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].title").value("Special Offer"))
           .andExpect(jsonPath("$[0].offerType").value("Discount"));

    // Then
    verify(offerService).getOffersByNetwork(networkId);
}

    @Test
void shouldGetOffersByNetwork_returns200_emptyList() throws Exception {
    // Given
    when(offerService.getOffersByNetwork(anyLong())).thenReturn(Collections.emptyList());

    // When
    mockMvc.perform(get("/api/v1/offers/network/{networkId}", 1L))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$").isEmpty());

    // Then
    verify(offerService).getOffersByNetwork(1L);
}

    @Test
void shouldSearchOffers_returns200() throws Exception {
    // Given
    OfferSummaryDTO offerSummary = new OfferSummaryDTO();
    offerSummary.setId(1L);
    offerSummary.setTitle("Sample Offer");
    offerSummary.setMerchantName("Sample Merchant");
    offerSummary.setOfferType("Discount");
    offerSummary.setDiscountPercentage(new BigDecimal("10.00"));
    offerSummary.setStartDate(LocalDate.now());
    offerSummary.setEndDate(LocalDate.now().plusDays(30));
    offerSummary.setActive(true);
    List<OfferSummaryDTO> offers = Collections.singletonList(offerSummary);
    when(offerService.searchOffers(any(String.class), any(String.class), any(String.class))).thenReturn(offers);

    // When
    mockMvc.perform(get("/api/v1/offers/search"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].id").value(1L))
           .andExpect(jsonPath("$[0].title").value("Sample Offer"))
           .andExpect(jsonPath("$[0].merchantName").value("Sample Merchant"))
           .andExpect(jsonPath("$[0].offerType").value("Discount"))
           .andExpect(jsonPath("$[0].discountPercentage").value(10.00))
           .andExpect(jsonPath("$[0].active").value(true));

    // Then
    verify(offerService).searchOffers(any(String.class), any(String.class), any(String.class));
}

}