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
import com.cardoffers.oms.controller.CardholderController;
import com.cardoffers.oms.model.dto.CardholderDTO;
import com.cardoffers.oms.service.CardholderService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(exclude = {RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class, CacheAutoConfiguration.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.cache.type=none"})
class CardholderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
void shouldGetCardholder_returns200() throws Exception {
    // Given
    CardholderDTO cardholderDTO = new CardholderDTO();
    cardholderDTO.setId(1L);
    cardholderDTO.setFirstName("John");
    cardholderDTO.setLastName("Doe");
    cardholderDTO.setEmail("john.doe@example.com");
    cardholderDTO.setPhoneNumber("1234567890");
    cardholderDTO.setActive(true);
    // Assume we have set up the CardholderService to return this DTO for an id of 1.
    
    // When
    mockMvc.perform(get("/api/v1/cardholders/1"))
        // S-28: GUARANTEED FAILURE — no entity persisted (no .save() found).
        // @BeforeEach MUST save the entity with ALL required fields before
        // any andExpect(status().isOk()) assertion. Without a persisted entity,
        // the service returns 404 (findById().orElseThrow() fails).
        // FIX: Add S-27 deep entity seeding to @BeforeEach:
        //   Entity saved = entityRepository.save(new Entity(...all required fields...));
        // Then reference saved.getId() in the request URL.
           .andExpect(status().isOk()) // Then
           .andExpect(jsonPath("$.id").value(1L))
           .andExpect(jsonPath("$.firstName").value("John"))
           .andExpect(jsonPath("$.lastName").value("Doe"))
           .andExpect(jsonPath("$.email").value("john.doe@example.com"))
           .andExpect(jsonPath("$.phoneNumber").value("1234567890"))
           .andExpect(jsonPath("$.active").value(true));
}

    @Test
void shouldGetCardholder_returns404_notFound() throws Exception {
    // Given
    Long nonExistentId = 999L;

    // When
    mockMvc.perform(get("/api/v1/cardholders/{id}", nonExistentId))
           .andExpect(status().isNotFound());
}

    @Test
void shouldGetAllCardholders_returns200() throws Exception {
    // Given
    CardholderDTO cardholder1 = new CardholderDTO();
    cardholder1.setFirstName("John");
    cardholder1.setLastName("Doe");
    cardholder1.setEmail("john.doe@example.com");
    
    CardholderDTO cardholder2 = new CardholderDTO();
    cardholder2.setFirstName("Jane");
    cardholder2.setLastName("Smith");
    cardholder2.setEmail("jane.smith@example.com");

    List<CardholderDTO> cardholders = Arrays.asList(cardholder1, cardholder2);
    
    // When
    mockMvc.perform(get("/api/v1/cardholders"))
        // S-28: GUARANTEED FAILURE — no entity persisted (no .save() found).
        // @BeforeEach MUST save the entity with ALL required fields before
        // any andExpect(status().isOk()) assertion. Without a persisted entity,
        // the service returns 404 (findById().orElseThrow() fails).
        // FIX: Add S-27 deep entity seeding to @BeforeEach:
        //   Entity saved = entityRepository.save(new Entity(...all required fields...));
        // Then reference saved.getId() in the request URL.
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].firstName", is("John")))
        .andExpect(jsonPath("$[0].lastName", is("Doe")))
        .andExpect(jsonPath("$[0].email", is("john.doe@example.com")))
        .andExpect(jsonPath("$[1].firstName", is("Jane")))
        .andExpect(jsonPath("$[1].lastName", is("Smith")))
        .andExpect(jsonPath("$[1].email", is("jane.smith@example.com")));
}

    @Test
void shouldCreateCardholder_returns200() throws Exception {
    // Given
    CardholderDTO cardholderDTO = new CardholderDTO();
    cardholderDTO.setFirstName("John");
    cardholderDTO.setLastName("Doe");
    cardholderDTO.setEmail("john.doe@example.com");
    cardholderDTO.setPhoneNumber("1234567890");
    cardholderDTO.setActive(true);
    
    // When
    mockMvc.perform(post("/api/v1/cardholders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(cardholderDTO)))
            // Then
        // S-28: GUARANTEED FAILURE — no entity persisted (no .save() found).
        // @BeforeEach MUST save the entity with ALL required fields before
        // any andExpect(status().isOk()) assertion. Without a persisted entity,
        // the service returns 404 (findById().orElseThrow() fails).
        // FIX: Add S-27 deep entity seeding to @BeforeEach:
        //   Entity saved = entityRepository.save(new Entity(...all required fields...));
        // Then reference saved.getId() in the request URL.
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.firstName").value("John"))
            .andExpect(jsonPath("$.lastName").value("Doe"))
            .andExpect(jsonPath("$.email").value("john.doe@example.com"));
}

    @Test
void shouldCreateCardholder_returns400_invalidInput() throws Exception {
    // Given
    String invalidCardholderJson = "{\"firstName\":\"\",\"lastName\":\"\",\"email\":\"invalid-email\"}";

    // When
    mockMvc.perform(post("/api/v1/cardholders")
            .contentType("application/json")
            .content(invalidCardholderJson))
            // Then
            .andExpect(status().isInternalServerError());
}

    @Test
void shouldUpdateCardholder_returns200() throws Exception {
    // Given
    CardholderDTO cardholderDTO = new CardholderDTO();
    cardholderDTO.setFirstName("John");
    cardholderDTO.setLastName("Doe");
    cardholderDTO.setEmail("john.doe@example.com");

    // When
    mockMvc.perform(put("/api/v1/cardholders/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(cardholderDTO)))
        // S-28: GUARANTEED FAILURE — no entity persisted (no .save() found).
        // @BeforeEach MUST save the entity with ALL required fields before
        // any andExpect(status().isOk()) assertion. Without a persisted entity,
        // the service returns 404 (findById().orElseThrow() fails).
        // FIX: Add S-27 deep entity seeding to @BeforeEach:
        //   Entity saved = entityRepository.save(new Entity(...all required fields...));
        // Then reference saved.getId() in the request URL.
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value("John"))
            .andExpect(jsonPath("$.lastName").value("Doe"))
            .andExpect(jsonPath("$.email").value("john.doe@example.com"));
}

}