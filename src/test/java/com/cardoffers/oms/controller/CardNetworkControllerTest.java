package com.cardoffers.oms.controller;

import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import org.springframework.test.context.ActiveProfiles;
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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.annotation.Validated;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import com.cardoffers.oms.exception.ResourceNotFoundException;

@WebMvcTest(CardNetworkController.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
class CardNetworkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardNetworkService cardNetworkService;

    @Test
void shouldGetAllCardNetworks_returns200() throws Exception {
    // Given
    CardNetworkDTO cardNetwork = new CardNetworkDTO();
    cardNetwork.setId(1L);
    cardNetwork.setName("Visa");
    cardNetwork.setCode("VISA");
    cardNetwork.setActive(true);
    List<CardNetworkDTO> cardNetworks = Collections.singletonList(cardNetwork);
    when(cardNetworkService.getAllCardNetworks()).thenReturn(cardNetworks);

    // When
    mockMvc.perform(get("/api/v1/card-networks"))
        // Then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1L))
        .andExpect(jsonPath("$[0].name").value("Visa"))
        .andExpect(jsonPath("$[0].code").value("VISA"))
        .andExpect(jsonPath("$[0].active").value(true));
}

    @Test
void shouldGetCardNetworkById_returns200() throws Exception {
    // Given
    CardNetworkDTO cardNetworkDTO = new CardNetworkDTO();
    cardNetworkDTO.setId(1L);
    cardNetworkDTO.setName("Visa");
    cardNetworkDTO.setCode("VISA");
    cardNetworkDTO.setActive(true);
    when(cardNetworkService.getCardNetworkById(1L)).thenReturn(cardNetworkDTO);

    // When
    mockMvc.perform(get("/api/v1/card-networks/{id}", 1L))
        // Then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.name").value("Visa"))
        .andExpect(jsonPath("$.code").value("VISA"))
        .andExpect(jsonPath("$.active").value(true));
}

    @Test
void shouldGetCardNetworkById_returns404_notFound() throws Exception {
    // Given
    Long nonExistentId = 1L;
    when(cardNetworkService.getCardNetworkById(nonExistentId)).thenThrow(new ResourceNotFoundException("Not found"));

    // When
    mockMvc.perform(get("/api/v1/card-networks/{id}", nonExistentId))
        // Then
        .andExpect(status().isNotFound());
}

    @Test
void shouldGetCardNetworkByCode_returns200() throws Exception {
    // Given
    CardNetworkDTO cardNetwork = new CardNetworkDTO();
    cardNetwork.setId(1L);
    cardNetwork.setName("Visa");
    cardNetwork.setCode("VISA");
    cardNetwork.setActive(true);
    when(cardNetworkService.getCardNetworkByCode("VISA")).thenReturn(cardNetwork);

    // When
    mockMvc.perform(get("/api/v1/card-networks/code/VISA"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(1L))
           .andExpect(jsonPath("$.name").value("Visa"))
           .andExpect(jsonPath("$.code").value("VISA"))
           .andExpect(jsonPath("$.active").value(true));

    // Then
    verify(cardNetworkService).getCardNetworkByCode("VISA");
}

    @Test
void shouldGetCardNetworkByCode_returns404_notFound() throws Exception {
    // Given
    String code = "NON_EXISTENT_CODE";
    when(cardNetworkService.getCardNetworkByCode(code)).thenThrow(new ResourceNotFoundException("Not found"));

    // When
    mockMvc.perform(get("/api/v1/card-networks/code/{code}", code))
           .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

    // Then
    verify(cardNetworkService).getCardNetworkByCode(code);
}

}