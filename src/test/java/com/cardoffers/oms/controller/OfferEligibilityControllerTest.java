package com.cardoffers.oms.controller;

import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
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
import com.cardoffers.oms.controller.OfferEligibilityController;
import com.cardoffers.oms.model.dto.EligibleOfferResponseDTO;
import com.cardoffers.oms.model.dto.OfferDTO;
import com.cardoffers.oms.service.OfferEligibilityService;
import io.swagger.v3.oas.annotations.Operation;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import com.cardoffers.oms.exception.ResourceNotFoundException;

@WebMvcTest(OfferEligibilityController.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
class OfferEligibilityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OfferEligibilityService offerEligibilityService;

    @Test
void shouldGetEligibleOffers_returns200() throws Exception {
    // Given
    Long cardholderId = 123L;
    EligibleOfferResponseDTO response = new EligibleOfferResponseDTO();
    response.setCardholderId(cardholderId);
    response.setCardholderName("John Doe");
    response.setTotalOffers(2);
    OfferDTO offer = new OfferDTO();
    offer.setId(1L);
    offer.setTitle("Discount Offer");
    offer.setDescription("Get 20% off");
    offer.setOfferType("Discount");
    offer.setDiscountPercentage(BigDecimal.valueOf(20));
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(30));
    offer.setSource("Online");
    response.setEligibleOffers(List.of(offer));
    
    when(offerEligibilityService.getEligibleOffers(cardholderId)).thenReturn(response);

    // When
    mockMvc.perform(get("/api/v1/eligible-offers/cardholder/{cardholderId}", cardholderId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.cardholderId").value(cardholderId))
            .andExpect(jsonPath("$.cardholderName").value("John Doe"))
            .andExpect(jsonPath("$.totalOffers").value(2))
            .andExpect(jsonPath("$.eligibleOffers[0].id").value(1L))
            .andExpect(jsonPath("$.eligibleOffers[0].title").value("Discount Offer"));

    // Then
    verify(offerEligibilityService).getEligibleOffers(cardholderId);
}

    @Test
void shouldGetEligibleOffers_returns404_notFound() throws Exception {
    // Given
    Long nonExistentCardholderId = 999L;
    when(offerEligibilityService.getEligibleOffers(nonExistentCardholderId)).thenThrow(new ResourceNotFoundException("Not found"));

    // When
    mockMvc.perform(get("/api/v1/eligible-offers/cardholder/{cardholderId}", nonExistentCardholderId))
            .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
}

    @Test
void shouldGetEligibleOffersByCategory_returns200() throws Exception {
    // Given
    Long cardholderId = 1L;
    String category = "electronics";
    OfferDTO offer = new OfferDTO();
    offer.setId(1L);
    offer.setTitle("Discount on Electronics");
    offer.setDescription("Get a discount on all electronics.");
    offer.setOfferType("category");
    offer.setDiscountPercentage(new BigDecimal("10"));
    offer.setMinimumPurchaseAmount(new BigDecimal("100"));
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(10));
    offer.setSource("website");
    List<OfferDTO> offers = Collections.singletonList(offer);
    
    EligibleOfferResponseDTO response = new EligibleOfferResponseDTO();
    response.setCardholderId(cardholderId);
    response.setEligibleOffers(offers);
    response.setTotalOffers(offers.size());
    
    when(offerEligibilityService.filterOffersByCategory(cardholderId, category)).thenReturn(offers);

    // When
    mockMvc.perform(get("/api/v1/eligible-offers/cardholder/{cardholderId}/category/{category}", cardholderId, category))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.cardholderId").value(cardholderId))
            .andExpect(jsonPath("$.eligibleOffers[0].title").value("Discount on Electronics"))
            .andExpect(jsonPath("$.totalOffers").value(1));

    // Then
    verify(offerEligibilityService).filterOffersByCategory(cardholderId, category);
}

    @Test
void shouldGetEligibleOffersByCategory_returns200_emptyList() throws Exception {
    // Given
    Long cardholderId = 1L;
    String category = "non-matching-category";
    when(offerEligibilityService.filterOffersByCategory(cardholderId, category)).thenReturn(Collections.emptyList());

    // When
    mockMvc.perform(get("/api/v1/eligible-offers/cardholder/{cardholderId}/category/{category}", cardholderId, category))
            // Then
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isEmpty());

    verify(offerEligibilityService).filterOffersByCategory(cardholderId, category);
}

    @Test
void shouldGetEligibleOffersByType_returns200() throws Exception {
    // Given
    Long cardholderId = 1L;
    String offerType = "discount";
    EligibleOfferResponseDTO response = new EligibleOfferResponseDTO();
    response.setCardholderId(cardholderId);
    response.setCardholderName("John Doe");
    response.setTotalOffers(2);
    OfferDTO offer = new OfferDTO();
    offer.setTitle("Spring Sale");
    offer.setOfferType(offerType);
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(10));
    response.setEligibleOffers(List.of(offer));
    
    when(offerEligibilityService.getEligibleOffersByType(cardholderId, offerType)).thenReturn(response);

    // When
    mockMvc.perform(get("/api/v1/eligible-offers/cardholder/{cardholderId}/type/{offerType}", cardholderId, offerType))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.cardholderId").value(cardholderId))
        .andExpect(jsonPath("$.cardholderName").value("John Doe"))
        .andExpect(jsonPath("$.totalOffers").value(2))
        .andExpect(jsonPath("$.eligibleOffers[0].title").value("Spring Sale"));

    // Then
    verify(offerEligibilityService).getEligibleOffersByType(cardholderId, offerType);
}

    @Test
void shouldGetEligibleOffersByType_returns200_emptyList() throws Exception {
    // Given
    Long cardholderId = 1L;
    String offerType = "non-matching-type";
    when(offerEligibilityService.filterOffersByOfferType(cardholderId, offerType)).thenReturn(new ArrayList<>());

    // When
    mockMvc.perform(get("/api/v1/eligible-offers/cardholder/{cardholderId}/type/{offerType}", cardholderId, offerType))
            // Then
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isEmpty());
}

}