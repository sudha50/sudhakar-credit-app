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
import com.cardoffers.oms.exception.ResourceNotFoundException;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.cache.type=none",
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration,org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration"
    })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class OfferControllerFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OfferService offerService;

    @Test
void shouldGetAllActiveOffers_returns200() throws Exception {
    // Given
    List<OfferSummaryDTO> offers = new ArrayList<>();
    OfferSummaryDTO offer1 = new OfferSummaryDTO();
    offer1.setId(1L);
    offer1.setTitle("Offer 1");
    offers.add(offer1);
    
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
           .andExpect(jsonPath("$[0].title").value("Offer 1"));

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
    offer.setSource("Web");
    when(offerService.getOfferById(1L)).thenReturn(offer);

    // When
    mockMvc.perform(get("/api/v1/offers/1"))
        // Then
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
}

    @Test
void shouldGetOfferById_returns404_notFound() throws Exception {
    // Given
    Long nonExistentId = 999L;
    when(offerService.getOfferById(nonExistentId)).thenThrow(new ResourceNotFoundException("Not found"));

    // When
    mockMvc.perform(get("/api/v1/offers/{id}", nonExistentId))
            .andExpect(status().isNotFound());
}

    @Test
void shouldGetOffersByMerchant_returns200() throws Exception {
    // Given
    Long merchantId = 1L;
    OfferSummaryDTO offer1 = new OfferSummaryDTO();
    offer1.setId(1L);
    offer1.setTitle("Special Offer");
    offer1.setMerchantName("Merchant A");
    offer1.setOfferType("Discount");
    offer1.setDiscountPercentage(new BigDecimal("10"));
    offer1.setStartDate(LocalDate.now());
    offer1.setEndDate(LocalDate.now().plusDays(10));
    offer1.setActive(true);
    
    List<OfferSummaryDTO> offers = List.of(offer1);
    when(offerService.getOffersByMerchant(merchantId)).thenReturn(offers);

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
           .andExpect(jsonPath("$[0].discountPercentage").value(10))
           .andExpect(jsonPath("$[0].startDate").value(LocalDate.now().toString()))
           .andExpect(jsonPath("$[0].endDate").value(LocalDate.now().plusDays(10).toString()))
           .andExpect(jsonPath("$[0].active").value(true));
}

    @Test
void shouldGetOffersByMerchant_returns200_emptyList() throws Exception {
    // Given
    Long merchantId = 123L;
    when(offerService.getOffersByMerchant(merchantId)).thenReturn(Collections.emptyList());

    // When & Then
    mockMvc.perform(get("/api/v1/offers/merchant/{merchantId}", merchantId))
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
    OfferSummaryDTO offerSummary = new OfferSummaryDTO();
    offerSummary.setId(1L);
    offerSummary.setTitle("Special Offer");
    offerSummary.setMerchantName("Merchant A");
    offerSummary.setOfferType("Discount");
    offerSummary.setDiscountPercentage(BigDecimal.valueOf(20));
    offerSummary.setStartDate(LocalDate.now());
    offerSummary.setEndDate(LocalDate.now().plusDays(7));
    offerSummary.setActive(true);
    
    List<OfferSummaryDTO> offers = Collections.singletonList(offerSummary);
    when(offerService.getOffersByNetwork(1L)).thenReturn(offers);

    // When
    mockMvc.perform(get("/api/v1/offers/network/{networkId}", 1L))
        // S-28: GUARANTEED FAILURE — no entity persisted (no .save() found).
        // @BeforeEach MUST save the entity with ALL required fields before
        // any andExpect(status().isOk()) assertion. Without a persisted entity,
        // the service returns 404 (findById().orElseThrow() fails).
        // FIX: Add S-27 deep entity seeding to @BeforeEach:
        //   Entity saved = entityRepository.save(new Entity(...all required fields...));
        // Then reference saved.getId() in the request URL.
           .andExpect(status().isOk())
           .andExpect(content().json("[{'id':1,'title':'Special Offer','merchantName':'Merchant A','offerType':'Discount','discountPercentage':20,'startDate':'" + LocalDate.now() + "','endDate':'" + LocalDate.now().plusDays(7) + "','active':true}]"));

    // Then
    verify(offerService).getOffersByNetwork(1L);
}

}