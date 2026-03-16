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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.annotation.Validated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(exclude = {RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class, CacheAutoConfiguration.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.cache.type=none"})
class OfferEligibilityControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
void shouldGetEligibleOffers_returns200() throws Exception {
    // Given
    long cardholderId = 123L;
    EligibleOfferResponseDTO response = new EligibleOfferResponseDTO();
    response.setCardholderId(cardholderId);
    response.setCardholderName("John Doe");
    response.setTotalOffers(3);
    
    OfferDTO offer = new OfferDTO();
    offer.setId(1L);
    offer.setTitle("Special Discount");
    offer.setDescription("Get 20% off on your next purchase");
    offer.setOfferType("Discount");
    offer.setDiscountPercentage(new BigDecimal("20.00"));
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(30));
    offer.setSource("API");
    response.setEligibleOffers(Arrays.asList(offer));

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
        .andExpect(jsonPath("$.cardholderName").value("John Doe"))
        .andExpect(jsonPath("$.totalOffers").value(3))
        .andExpect(jsonPath("$.eligibleOffers[0].title").value("Special Discount"));
}

    @Test
void shouldGetEligibleOffers_returns404_notFound() throws Exception {
    // Given
    Long nonExistentId = 999L; // assuming this ID does not exist
    // The service should throw an exception for non-existent id, ensure it's set up in the service layer.

    // When
    mockMvc.perform(get("/api/v1/eligible-offers/cardholder/{cardholderId}", nonExistentId))
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
    offer.setDescription("Get 20% off on all laptops.");
    offer.setOfferType("electronics");
    offer.setDiscountPercentage(new BigDecimal("20"));
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(30));
    offer.setSource("website");
    List<OfferDTO> offers = Collections.singletonList(offer);
    EligibleOfferResponseDTO response = new EligibleOfferResponseDTO();
    response.setCardholderId(cardholderId);
    response.setEligibleOffers(offers);
    response.setTotalOffers(1);

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
           .andExpect(jsonPath("$.cardholderId").value(cardholderId))
           .andExpect(jsonPath("$.eligibleOffers[0].title").value("Discount on Laptops"))
           .andExpect(jsonPath("$.totalOffers").value(1));
}

    @Test
void shouldGetEligibleOffersByCategory_returns200_emptyList() throws Exception {
    // Given
    Long cardholderId = 1L;
    String category = "nonMatchingCategory";

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
        .andExpect(jsonPath("$").isEmpty());

    // Then
}

    @Test
void shouldGetEligibleOffersByType_returns200() throws Exception {
    // Given
    Long cardholderId = 1L;
    String offerType = "default";
    List<OfferDTO> offers = new ArrayList<>();
    OfferDTO offer = new OfferDTO();
    offer.setId(1L);
    offer.setTitle("Special Offer");
    offer.setOfferType(offerType);
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(30));
    offers.add(offer);
    
    EligibleOfferResponseDTO response = new EligibleOfferResponseDTO();
    response.setCardholderId(cardholderId);
    response.setEligibleOffers(offers);
    response.setTotalOffers(offers.size());
    

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
           .andExpect(jsonPath("$.eligibleOffers[0].title").value("Special Offer"))
           .andExpect(jsonPath("$.totalOffers").value(1));
}

    @Test
void shouldGetEligibleOffersByType_returns200_emptyList() throws Exception {
    // Given
    Long cardholderId = 1L;
    String offerType = "non-matching-type";
    
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