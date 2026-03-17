package com.cardoffers.oms.controller;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import com.cardoffers.oms.exception.ResourceNotFoundException;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.cache.type=none",
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration,org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration"
    })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class OfferEligibilityControllerFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OfferEligibilityService offerEligibilityService;

    @Test
void shouldGetEligibleOffers_returns200() throws Exception {
    // Given
    Long cardholderId = 1L;
    EligibleOfferResponseDTO response = new EligibleOfferResponseDTO();
    response.setCardholderId(cardholderId);
    response.setCardholderName("Alice");
    response.setTotalOffers(1);
    OfferDTO offer = new OfferDTO();
    offer.setId(1L);
    offer.setTitle("Special Discount");
    offer.setOfferType("DISCOUNT");
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(10));
    response.setEligibleOffers(Arrays.asList(offer));
    when(offerEligibilityService.getEligibleOffers(cardholderId)).thenReturn(response);

    // When
    mockMvc.perform(get("/api/v1/eligible-offers/cardholder/{cardholderId}", cardholderId))
        // S-28: GUARANTEED FAILURE — no entity persisted (no .save() found).
        // @BeforeEach MUST save the entity with ALL required fields before
        // any andExpect(status().isOk()) assertion. Without a persisted entity,
        // the service returns 404 (findById().orElseThrow() fails).
        // FIX: Add S-27 deep entity seeding to @BeforeEach:
        //   Entity saved = entityRepository.save(new Entity(...all required fields...));
        // Then reference saved.getId() in the request URL.
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.cardholderId").value(cardholderId))
        .andExpect(jsonPath("$.cardholderName").value("Alice"))
        .andExpect(jsonPath("$.totalOffers").value(1))
        .andExpect(jsonPath("$.eligibleOffers[0].id").value(1L))
        .andExpect(jsonPath("$.eligibleOffers[0].title").value("Special Discount"));

    // Then
    verify(offerEligibilityService).getEligibleOffers(cardholderId);
}

    @Test
void shouldGetEligibleOffers_returns404_notFound() throws Exception {
    // Given
    Long cardholderId = 123L;
    when(offerEligibilityService.getEligibleOffers(cardholderId)).thenThrow(new ResourceNotFoundException("Offer eligibility not found for cardholderId: " + cardholderId));

    // When
    mockMvc.perform(get("/api/v1/eligible-offers/cardholder/{cardholderId}", cardholderId))
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
void shouldGetEligibleOffersByCategory_returns200() throws Exception {
    // Given
    Long cardholderId = 1L;
    String category = "electronics";
    OfferDTO offer = new OfferDTO();
    offer.setId(1L);
    offer.setTitle("Discount on Laptops");
    offer.setOfferType("CATEGORY");
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(30));
    when(offerEligibilityService.filterOffersByCategory(cardholderId, category)).thenReturn(List.of(offer));

    // When
    mockMvc.perform(get("/api/v1/eligible-offers/cardholder/{cardholderId}/category/{category}", cardholderId, category))
        // S-28: GUARANTEED FAILURE — no entity persisted (no .save() found).
        // @BeforeEach MUST save the entity with ALL required fields before
        // any andExpect(status().isOk()) assertion. Without a persisted entity,
        // the service returns 404 (findById().orElseThrow() fails).
        // FIX: Add S-27 deep entity seeding to @BeforeEach:
        //   Entity saved = entityRepository.save(new Entity(...all required fields...));
        // Then reference saved.getId() in the request URL.
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.eligibleOffers[0].id").value(1L))
            .andExpect(jsonPath("$.eligibleOffers[0].title").value("Discount on Laptops"));

    // Then
    verify(offerEligibilityService).filterOffersByCategory(cardholderId, category);
}

    @Test
void shouldGetEligibleOffersByCategory_returns200_emptyList() throws Exception {
    // Given
    Long cardholderId = 1L;
    String category = "NonMatchingCategory";
    when(offerEligibilityService.filterOffersByCategory(cardholderId, category)).thenReturn(Collections.emptyList());

    // When
    mockMvc.perform(get("/api/v1/eligible-offers/cardholder/{cardholderId}/category/{category}", cardholderId, category))
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
void shouldGetEligibleOffersByType_returns200() throws Exception {
    // Given
    Long cardholderId = 1L;
    String offerType = "discount";
    OfferDTO offer = new OfferDTO();
    offer.setId(1L);
    offer.setTitle("Special Discount");
    offer.setOfferType(offerType);
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(10));
    EligibleOfferResponseDTO response = new EligibleOfferResponseDTO();
    response.setCardholderId(cardholderId);
    response.setEligibleOffers(List.of(offer));
    response.setTotalOffers(1);
    
    when(offerEligibilityService.filterOffersByOfferType(cardholderId, offerType)).thenReturn(response.getEligibleOffers());

    // When
    mockMvc.perform(get("/api/v1/eligible-offers/cardholder/{cardholderId}/type/{offerType}", cardholderId, offerType))
        // S-28: GUARANTEED FAILURE — no entity persisted (no .save() found).
        // @BeforeEach MUST save the entity with ALL required fields before
        // any andExpect(status().isOk()) assertion. Without a persisted entity,
        // the service returns 404 (findById().orElseThrow() fails).
        // FIX: Add S-27 deep entity seeding to @BeforeEach:
        //   Entity saved = entityRepository.save(new Entity(...all required fields...));
        // Then reference saved.getId() in the request URL.
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.cardholderId").value(cardholderId))
        .andExpect(jsonPath("$.eligibleOffers[0].title").value("Special Discount"))
        .andExpect(jsonPath("$.totalOffers").value(1));
}

    @Test
void shouldGetEligibleOffersByType_returns200_emptyList() throws Exception {
    // Given
    Long cardholderId = 1L;
    String offerType = "non-matching-type";
    when(offerEligibilityService.filterOffersByOfferType(cardholderId, offerType)).thenReturn(Collections.emptyList());

    // When
    mockMvc.perform(get("/api/v1/eligible-offers/cardholder/{cardholderId}/type/{offerType}", cardholderId, offerType))
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

}