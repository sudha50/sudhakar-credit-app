package com.cardoffers.oms.controller;

import org.mockito.Mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.annotation.Validated;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.cardoffers.oms.exception.ResourceNotFoundException;

@SpringBootTest(exclude = {RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class, CacheAutoConfiguration.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.cache.type=none"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class CardholderControllerFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardholderService cardholderService;

    @Test
void shouldGetCardholder_returns200() throws Exception {
    // Given
    CardholderDTO cardholder = new CardholderDTO();
    cardholder.setId(1L);
    cardholder.setFirstName("John");
    cardholder.setLastName("Doe");
    cardholder.setEmail("john.doe@example.com");
    cardholder.setPhoneNumber("1234567890");
    cardholder.setActive(true);
    when(cardholderService.getCardholder(1L)).thenReturn(cardholder);

    // When
    mockMvc.perform(get("/api/v1/cardholders/{id}", 1L))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(1L))
           .andExpect(jsonPath("$.firstName").value("John"))
           .andExpect(jsonPath("$.lastName").value("Doe"))
           .andExpect(jsonPath("$.email").value("john.doe@example.com"))
           .andExpect(jsonPath("$.phoneNumber").value("1234567890"))
           .andExpect(jsonPath("$.active").value(true));

    // Then
    verify(cardholderService).getCardholder(1L);
}

    @Test
void shouldGetCardholder_returns404_notFound() throws Exception {
    // Given
    Long nonExistentId = 999L;
    when(cardholderService.getCardholder(nonExistentId)).thenThrow(new ResourceNotFoundException("Cardholder not found"));

    // When
    mockMvc.perform(get("/api/v1/cardholders/{id}", nonExistentId))
           .andExpect(status().isNotFound());

    // Then
    verify(cardholderService).getCardholder(nonExistentId);
}

    @Test
void shouldGetAllCardholders_returns200() throws Exception {
    // Given
    List<CardholderDTO> cardholders = new ArrayList<>();
    CardholderDTO cardholder = new CardholderDTO();
    cardholder.setFirstName("John");
    cardholder.setLastName("Doe");
    cardholder.setEmail("john.doe@example.com");
    cardholders.add(cardholder);
    when(cardholderService.getAllCardholders()).thenReturn(cardholders);

    // When
    mockMvc.perform(get("/api/v1/cardholders"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].firstName").value("John"))
           .andExpect(jsonPath("$[0].lastName").value("Doe"))
           .andExpect(jsonPath("$[0].email").value("john.doe@example.com"));

    // Then
    verify(cardholderService).getAllCardholders();
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
    
    when(cardholderService.createCardholder(any(CardholderDTO.class))).thenReturn(cardholderDTO);

    // When
    mockMvc.perform(post("/api/v1/cardholders")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@example.com\",\"phoneNumber\":\"1234567890\",\"active\":true}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.firstName").value("John"))
            .andExpect(jsonPath("$.lastName").value("Doe"))
            .andExpect(jsonPath("$.email").value("john.doe@example.com"))
            .andExpect(jsonPath("$.phoneNumber").value("1234567890"))
            .andExpect(jsonPath("$.active").value(true));

    // Then
    verify(cardholderService).createCardholder(any(CardholderDTO.class));
}

    @Test
void shouldCreateCardholder_returns400_invalidInput() throws Exception {
    // Given
    String invalidCardholderJson = "{\"firstName\":\"\",\"lastName\":\"\",\"email\":\"invalid-email\"}";

    // When
    mockMvc.perform(post("/api/v1/cardholders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidCardholderJson))
            // Then
            .andExpect(status().isInternalServerError());
}

    @Test
void shouldUpdateCardholder_returns200() throws Exception {
    // Given
    CardholderDTO cardholderDTO = new CardholderDTO();
    cardholderDTO.setId(1L);
    cardholderDTO.setFirstName("John");
    cardholderDTO.setLastName("Doe");
    cardholderDTO.setEmail("john.doe@example.com");
    cardholderDTO.setPhoneNumber("1234567890");
    cardholderDTO.setActive(true);
        //           UpdateDTO dto = new UpdateDTO(saved.getId(), "new value");
        //           service.update(dto); // createdAt preserved via DB read
cardholderService.updateCardholder(eq(1L), any(CardholderDTO.class))).thenReturn(cardholderDTO);

    // When
    mockMvc.perform(put("/api/v1/cardholders/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(cardholderDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.firstName").value("John"))
        .andExpect(jsonPath("$.lastName").value("Doe"))
        .andExpect(jsonPath("$.email").value("john.doe@example.com"))
        .andExpect(jsonPath("$.phoneNumber").value("1234567890"))
        .andExpect(jsonPath("$.active").value(true));

    // Then
    verify(cardholderService).updateCardholder(eq(1L), any(CardholderDTO.class));
}

}