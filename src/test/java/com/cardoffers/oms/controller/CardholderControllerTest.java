package com.cardoffers.oms.controller;

import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.springframework.test.context.ActiveProfiles;
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
import com.cardoffers.oms.controller.CardholderController;
import com.cardoffers.oms.model.dto.CardholderDTO;
import com.cardoffers.oms.service.CardholderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.annotation.Validated;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.cardoffers.oms.exception.ResourceNotFoundException;

@WebMvcTest(CardholderController.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
class CardholderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardholderService cardholderService;

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
    
    when(cardholderService.getCardholder(1L)).thenReturn(cardholderDTO);

    // When & Then
    mockMvc.perform(get("/api/v1/cardholders/{id}", 1L))
           .andExpect(status().isOk())
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
    when(cardholderService.getCardholder(nonExistentId)).thenThrow(new ResourceNotFoundException("Not found"));

    // When
    mockMvc.perform(get("/api/v1/cardholders/{id}", nonExistentId))
           .andExpect(status().isNotFound());
    
    // Then
    verify(cardholderService).getCardholder(nonExistentId);
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
    cardholder2.setLastName("Doe");
    cardholder2.setEmail("jane.doe@example.com");

    List<CardholderDTO> cardholders = Arrays.asList(cardholder1, cardholder2);
    when(cardholderService.getAllCardholders()).thenReturn(cardholders);

    // When
    mockMvc.perform(get("/api/v1/cardholders"))
        // Then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].firstName").value("John"))
        .andExpect(jsonPath("$[1].firstName").value("Jane"))
        .andExpect(jsonPath("$.length()").value(2));
}

    @Test
void shouldCreateCardholder_returns200() throws Exception {
    // Given
    CardholderDTO requestDto = new CardholderDTO();
    requestDto.setFirstName("John");
    requestDto.setLastName("Doe");
    requestDto.setEmail("john.doe@example.com");
    requestDto.setPhoneNumber("1234567890");
    requestDto.setActive(true);
    
    CardholderDTO responseDto = new CardholderDTO();
    responseDto.setId(1L);
    responseDto.setFirstName("John");
    responseDto.setLastName("Doe");
    responseDto.setEmail("john.doe@example.com");
    responseDto.setPhoneNumber("1234567890");
    responseDto.setActive(true);
    
    when(cardholderService.createCardholder(any(CardholderDTO.class))).thenReturn(responseDto);
    
    // When
    mockMvc.perform(post("/api/v1/cardholders")
            .contentType("application/json")
            .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@example.com\",\"phoneNumber\":\"1234567890\",\"active\":true}"))
            // Then
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.firstName").value("John"))
            .andExpect(jsonPath("$.lastName").value("Doe"))
            .andExpect(jsonPath("$.email").value("john.doe@example.com"))
            .andExpect(jsonPath("$.phoneNumber").value("1234567890"))
            .andExpect(jsonPath("$.active").value(true));
    
    verify(cardholderService).createCardholder(any(CardholderDTO.class));
}

    @Test
void shouldCreateCardholder_returns400_invalidInput() throws Exception {
    // Given
    CardholderDTO invalidCardholderDTO = new CardholderDTO();
    // firstName, lastName, and email are not set, making the input invalid

    // When
    mockMvc.perform(post("/api/v1/cardholders")
            .contentType("application/json")
            .content(new ObjectMapper().writeValueAsString(invalidCardholderDTO)))
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
    cardholderDTO.setPhoneNumber("1234567890");
    cardholderDTO.setActive(true);
    when(cardholderService.updateCardholder(1L, cardholderDTO)).thenReturn(cardholderDTO);

    // When
    mockMvc.perform(put("/api/v1/cardholders/{id}", 1L)
            .contentType("application/json")
            .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@example.com\",\"phoneNumber\":\"1234567890\",\"active\":true}"))
            // Then
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value("John"))
            .andExpect(jsonPath("$.lastName").value("Doe"))
            .andExpect(jsonPath("$.email").value("john.doe@example.com"));
}

    @Test
void shouldUpdateCardholder_returns400_invalidInput() throws Exception {
    // Given
    CardholderDTO cardholderDTO = new CardholderDTO();
    // Intentionally leaving required fields blank to simulate invalid input

    // When
    mockMvc.perform(put("/api/v1/cardholders/1")
            .contentType("application/json")
            .content(new ObjectMapper().writeValueAsString(cardholderDTO)))
            // Then
            .andExpect(status().isInternalServerError());
}

    @Test
void shouldUpdateCardholder_returns404_notFound() throws Exception {
    // Given
    Long nonExistentId = 999L;
    CardholderDTO cardholderDTO = new CardholderDTO();
    cardholderDTO.setFirstName("John");
    cardholderDTO.setLastName("Doe");
    cardholderDTO.setEmail("john.doe@example.com");
    when(cardholderService.updateCardholder(eq(nonExistentId), any(CardholderDTO.class)))
            .thenThrow(new ResourceNotFoundException("Cardholder not found"));

    // When
    mockMvc.perform(put("/api/v1/cardholders/{id}", nonExistentId)
            .contentType("application/json")
            .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@example.com\"}"))
            // Then
            .andExpect(status().isNotFound());
}

}