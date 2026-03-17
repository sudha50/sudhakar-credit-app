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

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.cache.type=none",
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration,org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration"
    })
class OfferEligibilityControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
void shouldGetEligibleOffers_returns200() throws Exception {
    // Given
    Long cardholderId = 1L;
    EligibleOfferResponseDTO response = new EligibleOfferResponseDTO();
    response.setCardholderId(cardholderId);
    response.setCardholderName("John Doe");
    OfferDTO offer = new OfferDTO();
    offer.setId(1L);
    offer.setTitle("Exclusive Offer");
    offer.setDescription("10% off on next purchase");
    offer.setOfferType("Discount");
    offer.setDiscountPercentage(new BigDecimal("10.00"));
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(30));
    offer.setSource("Website");
    response.setEligibleOffers(Collections.singletonList(offer));
    response.setTotalOffers(1);
    
    // Mock the service response (Assuming the service is wired correctly in the context)
    
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
        .andExpect(jsonPath("$.eligibleOffers[0].id").value(1))
        .andExpect(jsonPath("$.eligibleOffers[0].title").value("Exclusive Offer"))
        .andExpect(jsonPath("$.totalOffers").value(1));
}

    @Test
void shouldGetEligibleOffers_returns404_notFound() throws Exception {
    // Given
    Long nonExistentCardholderId = 999L; // assume this ID does not exist in the system

    // When
    mockMvc.perform(get("/api/v1/eligible-offers/cardholder/{cardholderId}", nonExistentCardholderId))
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
    String category = "food";
    OfferDTO offer = new OfferDTO();
    offer.setId(1L);
    offer.setTitle("Discount on Food");
    offer.setOfferType("Food");
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(30));
    offer.setSource("app");
    
    EligibleOfferResponseDTO response = new EligibleOfferResponseDTO();
    response.setCardholderId(cardholderId);
    response.setEligibleOffers(Arrays.asList(offer));
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
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$.cardholderId").value(cardholderId))
           .andExpect(jsonPath("$.totalOffers").value(1))
           .andExpect(jsonPath("$.eligibleOffers[0].title").value("Discount on Food"));

    // Then
}

    @Test
void shouldGetEligibleOffersByCategory_returns200_emptyList() throws Exception {
    // Given
    Long cardholderId = 1L;
    String category = "non-matching-category";

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
    offer.setDescription("Get a special discount!");
    offer.setOfferType(offerType);
    offer.setDiscountPercentage(new BigDecimal("10"));
    offer.setMinimumPurchaseAmount(new BigDecimal("50"));
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(10));
    offer.setSource("App");
    
    EligibleOfferResponseDTO response = new EligibleOfferResponseDTO();
    response.setCardholderId(cardholderId);
    response.setCardholderName("John Doe");
    response.setEligibleOffers(Collections.singletonList(offer));
    response.setTotalOffers(1);
    

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
           .andExpect(jsonPath("$.cardholderName").value("John Doe"))
           .andExpect(jsonPath("$.totalOffers").value(1))
           .andExpect(jsonPath("$.eligibleOffers[0].title").value("Special Discount"))
           .andExpect(jsonPath("$.eligibleOffers[0].offerType").value(offerType));
}

    @Test
void shouldGetEligibleOffersByType_returns200_emptyList() throws Exception {
    // Given
    Long cardholderId = 123L;
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