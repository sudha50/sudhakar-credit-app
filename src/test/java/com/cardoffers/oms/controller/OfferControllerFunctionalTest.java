package com.cardoffers.oms.controller;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.anyLong;
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
import com.cardoffers.oms.model.dto.CardNetworkDTO;
import com.cardoffers.oms.model.dto.MerchantDTO;
import com.cardoffers.oms.exception.ResourceNotFoundException;

@SpringBootTest(exclude = {RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class, CacheAutoConfiguration.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.cache.type=none"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class OfferControllerFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OfferService offerService;

    @Test
void shouldGetAllActiveOffers_returns200() throws Exception {
    // Given
    OfferSummaryDTO offer = new OfferSummaryDTO();
    offer.setId(1L);
    offer.setTitle("Special Offer");
    offer.setMerchantName("Merchant A");
    offer.setOfferType("Discount");
    offer.setDiscountPercentage(new BigDecimal("10"));
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(10));
    offer.setActive(true);
    List<OfferSummaryDTO> offers = Collections.singletonList(offer);
    when(offerService.getAllActiveOffers()).thenReturn(offers);

    // When
    mockMvc.perform(get("/api/v1/offers"))
        // S-28: GUARANTEED FAILURE — no entity persisted (no .save() found).
        // @BeforeEach MUST save the entity with ALL required fields before
        // any andExpect(status().isOk()) assertion. Without a persisted entity,
        // the service returns 404 (findById().orElseThrow() fails).
        // FIX: Add S-27 deep entity seeding to @BeforeEach:
        //   Entity saved = entityRepository.save(new Entity(...all required fields...));
        // Then reference saved.getId() in the request URL.
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].id").value(1L))
           .andExpect(jsonPath("$[0].title").value("Special Offer"))
           .andExpect(jsonPath("$[0].merchantName").value("Merchant A"))
           .andExpect(jsonPath("$[0].offerType").value("Discount"))
           .andExpect(jsonPath("$[0].discountPercentage").value(10))
           .andExpect(jsonPath("$[0].startDate").value(LocalDate.now().toString()))
           .andExpect(jsonPath("$[0].endDate").value(LocalDate.now().plusDays(10).toString()))
           .andExpect(jsonPath("$[0].active").value(true));

    // Then
    verify(offerService).getAllActiveOffers();
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
    MerchantDTO merchant = new MerchantDTO();
    merchant.setId(1L);
    offer.setMerchant(merchant);
    CardNetworkDTO cardNetwork = new CardNetworkDTO();
    cardNetwork.setId(1L);
    offer.setCardNetwork(cardNetwork);
    when(offerService.getOfferById(1L)).thenReturn(offer);

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
    verify(offerService).getOfferById(1L);
}

    @Test
void shouldGetOfferById_returns404_notFound() throws Exception {
    // Given
    Long nonExistentId = 99L;
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
    Long merchantId = 1L;
    OfferSummaryDTO offerSummary = new OfferSummaryDTO();
    offerSummary.setId(1L);
    offerSummary.setTitle("Special Offer");
    offerSummary.setMerchantName("Merchant A");
    offerSummary.setOfferType("Discount");
    offerSummary.setDiscountPercentage(new BigDecimal("20.00"));
    offerSummary.setStartDate(LocalDate.now());
    offerSummary.setEndDate(LocalDate.now().plusDays(30));
    offerSummary.setActive(true);
    List<OfferSummaryDTO> offerSummaries = Collections.singletonList(offerSummary);
    
    when(offerService.getOffersByMerchant(merchantId)).thenReturn(offerSummaries);

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
           .andExpect(jsonPath("$[0].id").value(1L))
           .andExpect(jsonPath("$[0].title").value("Special Offer"))
           .andExpect(jsonPath("$[0].merchantName").value("Merchant A"))
           .andExpect(jsonPath("$[0].offerType").value("Discount"))
           .andExpect(jsonPath("$[0].discountPercentage").value(20.00))
           .andExpect(jsonPath("$[0].startDate").value(LocalDate.now().toString()))
           .andExpect(jsonPath("$[0].endDate").value(LocalDate.now().plusDays(30).toString()))
           .andExpect(jsonPath("$[0].active").value(true));
}

    @Test
void shouldGetOffersByMerchant_returns200_emptyList() throws Exception {
    // Given
    when(offerService.getOffersByMerchant(anyLong())).thenReturn(Collections.emptyList());

    // When
    mockMvc.perform(get("/api/v1/offers/merchant/{merchantId}", 999L))
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
    OfferSummaryDTO offerSummary = new OfferSummaryDTO();
    offerSummary.setId(1L);
    offerSummary.setTitle("Offer Title");
    offerSummary.setMerchantName("Merchant Name");
    offerSummary.setOfferType("Discount");
    offerSummary.setDiscountPercentage(BigDecimal.valueOf(20));
    offerSummary.setStartDate(LocalDate.now());
    offerSummary.setEndDate(LocalDate.now().plusDays(30));
    offerSummary.setActive(true);
    
    List<OfferSummaryDTO> offers = Collections.singletonList(offerSummary);
    when(offerService.getOffersByNetwork(networkId)).thenReturn(offers);

    // When
    mockMvc.perform(get("/api/v1/offers/network/{networkId}", networkId))
        // S-28: GUARANTEED FAILURE — no entity persisted (no .save() found).
        // @BeforeEach MUST save the entity with ALL required fields before
        // any andExpect(status().isOk()) assertion. Without a persisted entity,
        // the service returns 404 (findById().orElseThrow() fails).
        // FIX: Add S-27 deep entity seeding to @BeforeEach:
        //   Entity saved = entityRepository.save(new Entity(...all required fields...));
        // Then reference saved.getId() in the request URL.
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].id").value(1L))
           .andExpect(jsonPath("$[0].title").value("Offer Title"))
           .andExpect(jsonPath("$[0].merchantName").value("Merchant Name"))
           .andExpect(jsonPath("$[0].offerType").value("Discount"))
           .andExpect(jsonPath("$[0].discountPercentage").value(20));
}

}