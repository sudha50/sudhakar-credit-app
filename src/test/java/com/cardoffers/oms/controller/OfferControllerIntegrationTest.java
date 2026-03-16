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
import org.springframework.data.domain.Example;
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

@SpringBootTest(exclude = {RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class, CacheAutoConfiguration.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.cache.type=none"})
class OfferControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
void shouldGetAllActiveOffers_returns200() throws Exception {
    // Given
    OfferDTO offer1 = new OfferDTO();
    offer1.setTitle("Summer Discount");
    offer1.setOfferType("Percentage");
    offer1.setStartDate(LocalDate.now());
    offer1.setEndDate(LocalDate.now().plusDays(30));
    offer1.setSource("Website");
    
    OfferDTO offer2 = new OfferDTO();
    offer2.setTitle("Winter Sale");
    offer2.setOfferType("Cashback");
    offer2.setStartDate(LocalDate.now());
    offer2.setEndDate(LocalDate.now().plusDays(30));
    offer2.setSource("App");

    List<OfferDTO> offers = Arrays.asList(offer1, offer2);

    // When
    mockMvc.perform(get("/api/v1/offers"))
        // S-28: GUARANTEED FAILURE — no entity persisted (no .save() found).
        // @BeforeEach MUST save the entity with ALL required fields before
        // any andExpect(status().isOk()) assertion. Without a persisted entity,
        // the service returns 404 (findById().orElseThrow() fails).
        // FIX: Add S-27 deep entity seeding to @BeforeEach:
        //   Entity saved = entityRepository.save(new Entity(...all required fields...));
        // Then reference saved.getId() in the request URL.
           .andExpect(status().is2xxSuccessful())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$", hasSize(2)))
           .andExpect(jsonPath("$[0].title", is("Summer Discount")))
           .andExpect(jsonPath("$[1].title", is("Winter Sale")));
}

    @Test
void shouldGetOfferById_returns200() throws Exception {
    // Given
    OfferDTO offer = new OfferDTO();
    offer.setId(1L);
    offer.setTitle("Special Discount");
    offer.setOfferType("Discount");
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(30));
    offer.setSource("Website");
    offer.setMerchant(new MerchantDTO());
    offer.setCardNetwork(new CardNetworkDTO());

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
           .andExpect(jsonPath("$.offerType").value("Discount"))
           .andExpect(jsonPath("$.startDate").value(LocalDate.now().toString()))
           .andExpect(jsonPath("$.endDate").value(LocalDate.now().plusDays(30).toString()));
}

    @Test
void shouldGetOfferById_returns404_notFound() throws Exception {
    // Given
    long nonExistentId = 999L; // Example ID that does not exist

    // When
    mockMvc.perform(get("/api/v1/offers/{id}", nonExistentId))
        .andExpect(status().isNotFound());
}

    @Test
void shouldGetOffersByMerchant_returns200() throws Exception {
    // Given
    Long merchantId = 1L;
    OfferDTO offer = new OfferDTO();
    offer.setTitle("Special Offer");
    offer.setOfferType("Discount");
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(30));
    offer.setMerchant(new MerchantDTO()); // Assuming MerchantDTO has a no-arg constructor
    offer.setCardNetwork(new CardNetworkDTO()); // Assuming CardNetworkDTO has a no-arg constructor

    // Set up database with the offer for the given merchantId (this part is abstracted)
    
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
            .andExpect(jsonPath("$[0].title").value(offer.getTitle()))
            .andExpect(jsonPath("$[0].offerType").value(offer.getOfferType()))
            .andExpect(jsonPath("$[0].startDate").value(offer.getStartDate().toString()))
            .andExpect(jsonPath("$[0].endDate").value(offer.getEndDate().toString()));
}

    @Test
void shouldGetOffersByMerchant_returns200_emptyList() throws Exception {
    // Given - service returns an empty list (mock behavior)
    
    // When
    mockMvc.perform(get("/api/v1/offers/merchant/9999")) // Assume 9999 is a non-matching merchantId
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
    OfferDTO offer = new OfferDTO();
    offer.setTitle("Special Offer");
    offer.setOfferType("Discount");
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(10));
    offer.setSource("Network");
    offer.setMerchant(new MerchantDTO()); // assuming MerchantDTO has a no-arg constructor
    offer.setCardNetwork(new CardNetworkDTO()); // assuming CardNetworkDTO has a no-arg constructor
    // Assuming the service is properly set up to return the valid response

    // When
    mockMvc.perform(get("/api/v1/offers/network/{networkId}", "networkId"))
        // S-28: GUARANTEED FAILURE — no entity persisted (no .save() found).
        // @BeforeEach MUST save the entity with ALL required fields before
        // any andExpect(status().isOk()) assertion. Without a persisted entity,
        // the service returns 404 (findById().orElseThrow() fails).
        // FIX: Add S-27 deep entity seeding to @BeforeEach:
        //   Entity saved = entityRepository.save(new Entity(...all required fields...));
        // Then reference saved.getId() in the request URL.
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].title").value("Special Offer"))
           .andExpect(jsonPath("$[0].offerType").value("Discount"))
           .andExpect(jsonPath("$[0].startDate").value(LocalDate.now().toString()))
           .andExpect(jsonPath("$[0].endDate").value(LocalDate.now().plusDays(10).toString()));
}

}