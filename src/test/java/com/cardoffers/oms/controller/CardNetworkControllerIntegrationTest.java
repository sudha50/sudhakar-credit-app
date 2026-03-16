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
import com.cardoffers.oms.controller.CardNetworkController;
import com.cardoffers.oms.model.dto.CardNetworkDTO;
import com.cardoffers.oms.service.CardNetworkService;
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
class CardNetworkControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
void shouldGetAllCardNetworks_returns200() throws Exception {
    // Given
    CardNetworkDTO cardNetwork1 = new CardNetworkDTO();
    cardNetwork1.setId(1L);
    cardNetwork1.setName("Visa");
    cardNetwork1.setCode("VISA");
    cardNetwork1.setActive(true);
    
    CardNetworkDTO cardNetwork2 = new CardNetworkDTO();
    cardNetwork2.setId(2L);
    cardNetwork2.setName("MasterCard");
    cardNetwork2.setCode("MASTERCARD");
    cardNetwork2.setActive(true);
    
    List<CardNetworkDTO> cardNetworks = Arrays.asList(cardNetwork1, cardNetwork2);
    
    // When
    mockMvc.perform(get("/api/v1/card-networks"))
        // S-28: GUARANTEED FAILURE — no entity persisted (no .save() found).
        // @BeforeEach MUST save the entity with ALL required fields before
        // any andExpect(status().isOk()) assertion. Without a persisted entity,
        // the service returns 404 (findById().orElseThrow() fails).
        // FIX: Add S-27 deep entity seeding to @BeforeEach:
        //   Entity saved = entityRepository.save(new Entity(...all required fields...));
        // Then reference saved.getId() in the request URL.
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].name").value("Visa"))
           .andExpect(jsonPath("$[1].name").value("MasterCard"))
           .andExpect(jsonPath("$[0].code").value("VISA"))
           .andExpect(jsonPath("$[1].code").value("MASTERCARD"));
}

    @Test
void shouldGetCardNetworkById_returns200() throws Exception {
    // Given
    CardNetworkDTO cardNetwork = new CardNetworkDTO();
    cardNetwork.setId(1L);
    cardNetwork.setName("Visa");
    cardNetwork.setCode("VISA");
    cardNetwork.setActive(true);

    // When
    mockMvc.perform(get("/api/v1/card-networks/1"))
        // S-28: GUARANTEED FAILURE — no entity persisted (no .save() found).
        // @BeforeEach MUST save the entity with ALL required fields before
        // any andExpect(status().isOk()) assertion. Without a persisted entity,
        // the service returns 404 (findById().orElseThrow() fails).
        // FIX: Add S-27 deep entity seeding to @BeforeEach:
        //   Entity saved = entityRepository.save(new Entity(...all required fields...));
        // Then reference saved.getId() in the request URL.
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(1L))
           .andExpect(jsonPath("$.name").value("Visa"))
           .andExpect(jsonPath("$.code").value("VISA"))
           .andExpect(jsonPath("$.active").value(true));

    // Then
}

    @Test
void shouldGetCardNetworkById_returns404_notFound() throws Exception {
    // Given
    Long nonExistentId = 999L;

    // When
    mockMvc.perform(get("/api/v1/card-networks/{id}", nonExistentId))
           .andExpect(status().isNotFound());
}

    @Test
void shouldGetCardNetworkByCode_returns200() throws Exception {
    // Given
    CardNetworkDTO cardNetworkDTO = new CardNetworkDTO();
    cardNetworkDTO.setId(1L);
    cardNetworkDTO.setName("Visa");
    cardNetworkDTO.setCode("VISA");
    cardNetworkDTO.setActive(true);

    // When
    mockMvc.perform(get("/api/v1/card-networks/code/VISA"))
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
        .andExpect(jsonPath("$.name").value("Visa"))
        .andExpect(jsonPath("$.code").value("VISA"))
        .andExpect(jsonPath("$.active").value(true));
}

    @Test
void shouldGetCardNetworkByCode_returns404_notFound() throws Exception {
    // Given
    String code = "nonexistent-code";

    // When & Then
    mockMvc.perform(get("/api/v1/card-networks/code/{code}", code))
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