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
import com.cardoffers.oms.controller.OfferController;
import com.cardoffers.oms.model.dto.OfferDTO;
import com.cardoffers.oms.model.dto.OfferSummaryDTO;
import com.cardoffers.oms.service.OfferService;
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
import com.cardoffers.oms.model.dto.CardNetworkDTO;
import com.cardoffers.oms.model.dto.MerchantDTO;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.cache.type=none",
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration,org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration"
    })
class OfferControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
void shouldGetAllActiveOffers_returns200() throws Exception {
    // Given
    OfferSummaryDTO offer1 = new OfferSummaryDTO();
    offer1.setId(1L);
    offer1.setTitle("Discount Offer");
    offer1.setMerchantName("Merchant A");
    offer1.setOfferType("Discount");
    offer1.setDiscountPercentage(new BigDecimal("10.00"));
    offer1.setStartDate(LocalDate.now());
    offer1.setEndDate(LocalDate.now().plusDays(30));
    offer1.setActive(true);

    OfferSummaryDTO offer2 = new OfferSummaryDTO();
    offer2.setId(2L);
    offer2.setTitle("Cashback Offer");
    offer2.setMerchantName("Merchant B");
    offer2.setOfferType("Cashback");
    offer2.setDiscountPercentage(new BigDecimal("5.00"));
    offer2.setStartDate(LocalDate.now());
    offer2.setEndDate(LocalDate.now().plusDays(30));
    offer2.setActive(true);


    // When
    mockMvc.perform(get("/api/v1/offers"))
        // Then
        // S-28: GUARANTEED FAILURE — no entity persisted (no .save() found).
        // @BeforeEach MUST save the entity with ALL required fields before
        // any andExpect(status().isOk()) assertion. Without a persisted entity,
        // the service returns 404 (findById().orElseThrow() fails).
        // FIX: Add S-27 deep entity seeding to @BeforeEach:
        //   Entity saved = entityRepository.save(new Entity(...all required fields...));
        // Then reference saved.getId() in the request URL.
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].title").value("Discount Offer"))
        .andExpect(jsonPath("$[1].title").value("Cashback Offer"));
}

    @Test
void shouldGetOfferById_returns200() throws Exception {
    // Given
    OfferDTO offer = new OfferDTO();
    offer.setId(1L);
    offer.setTitle("Special Discount");
    offer.setOfferType("Discount");
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(10));
    offer.setSource("Website");
    // create a mock service response (not mocked in integration)
    offerService.createOffer(offer); 

    // When
    mockMvc.perform(get("/api/v1/offers/{id}", 1L))
        // S-28: GUARANTEED FAILURE — no entity persisted (no .save() found).
        // @BeforeEach MUST save the entity with ALL required fields before
        // any andExpect(status().isOk()) assertion. Without a persisted entity,
        // the service returns 404 (findById().orElseThrow() fails).
        // FIX: Add S-27 deep entity seeding to @BeforeEach:
        //   Entity saved = entityRepository.save(new Entity(...all required fields...));
        // Then reference saved.getId() in the request URL.
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(1L))
           .andExpect(jsonPath("$.title").value("Special Discount"))
           .andExpect(jsonPath("$.offerType").value("Discount"));

    // Then
    // The assertions are handled in the When block with status and jsonPath checks
}

    @Test
void shouldGetOfferById_returns404_notFound() throws Exception {
    // Given
    Long nonExistentId = 999L;

    // When
    mockMvc.perform(get("/api/v1/offers/{id}", nonExistentId))
           .andExpect(status().isNotFound());
}

    @Test
void shouldGetOffersByMerchant_returns200() throws Exception {
    // Given
    Long merchantId = 1L;
    OfferDTO offer = new OfferDTO();
    offer.setId(1L);
    offer.setTitle("Sample Offer");
    offer.setOfferType("Discount");
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(10));
    offer.setSource("Online");
    offer.setMerchant(new MerchantDTO());
    offer.getMerchant().setId(merchantId);
    List<OfferDTO> offers = List.of(offer);

    // When
    mockMvc.perform(get("/api/v1/offers/merchant/{merchantId}", merchantId))
        // S-28: GUARANTEED FAILURE — no entity persisted (no .save() found).
        // @BeforeEach MUST save the entity with ALL required fields before
        // any andExpect(status().isOk()) assertion. Without a persisted entity,
        // the service returns 404 (findById().orElseThrow() fails).
        // FIX: Add S-27 deep entity seeding to @BeforeEach:
        //   Entity saved = entityRepository.save(new Entity(...all required fields...));
        // Then reference saved.getId() in the request URL.
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].title").value("Sample Offer"))
            .andExpect(jsonPath("$[0].offerType").value("Discount"));

    // Then
}

    @Test
void shouldGetOffersByMerchant_returns200_emptyList() throws Exception {
    // Given
    long merchantId = 1L;  // Non-matching filter

    // When
    mockMvc.perform(get("/api/v1/offers/merchant/{merchantId}", merchantId))
    
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
void shouldGetOffersByNetwork_returns200() throws Exception {
    // Given
    Long networkId = 1L;
    OfferDTO offer = new OfferDTO();
    offer.setId(1L);
    offer.setTitle("Special Discount");
    offer.setOfferType("Discount");
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(30));
    offer.setSource("Website");
    offer.setMerchant(new MerchantDTO()); // Assuming MerchantDTO and CardNetworkDTO are properly initialized
    offer.setCardNetwork(new CardNetworkDTO());

    List<OfferDTO> offers = Collections.singletonList(offer);

    // When
    mockMvc.perform(get("/api/v1/offers/network/{networkId}", networkId))
        // Then
        // S-28: GUARANTEED FAILURE — no entity persisted (no .save() found).
        // @BeforeEach MUST save the entity with ALL required fields before
        // any andExpect(status().isOk()) assertion. Without a persisted entity,
        // the service returns 404 (findById().orElseThrow() fails).
        // FIX: Add S-27 deep entity seeding to @BeforeEach:
        //   Entity saved = entityRepository.save(new Entity(...all required fields...));
        // Then reference saved.getId() in the request URL.
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].title").value("Special Discount"))
        .andExpect(jsonPath("$[0].offerType").value("Discount"));
}

}