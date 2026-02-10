package com.cardoffers.oms.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.Optional;

import com.cardoffers.oms.model.dto.CardholderDTO;
import com.cardoffers.oms.service.CardholderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import jakarta.validation.Valid;

@ExtendWith(MockitoExtension.class)
class CardholderControllerTest {

    @Mock
    private CardholderService cardholderService;

    @InjectMocks
    private CardholderController cardholderController;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cardholderController).build();
    }

    @Test
    void shouldReturnCardholder_whenValidIdProvided() throws Exception {
        CardholderDTO dto = new CardholderDTO();
        dto.setId(1L);
        when(cardholderService.getCardholderById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/cardholders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldReturnCardholderByEmail_whenValidEmailProvided() throws Exception {
        CardholderDTO dto = new CardholderDTO();
        dto.setEmail("test@example.com");
        when(cardholderService.getCardholderByEmail("test@example.com")).thenReturn(dto);

        mockMvc.perform(get("/api/v1/cardholders/email/test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void shouldReturnAllCardholders_whenGetAllInvoked() throws Exception {
        when(cardholderService.getAllActiveCardholders()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/cardholders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void shouldCreateCardholder_whenValidDtoProvided() throws Exception {
        CardholderDTO dto = new CardholderDTO();
        dto.setEmail("new@example.com");
        when(cardholderService.createCardholder(any(CardholderDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/api/v1/cardholders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"new@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("new@example.com"));
    }

    @Test
    void shouldUpdateCardholder_whenValidIdAndDtoProvided() throws Exception {
        CardholderDTO dto = new CardholderDTO();
        dto.setId(2L);
        dto.setEmail("updated@example.com");
        when(cardholderService.updateCardholder(eq(2L), any(CardholderDTO.class))).thenReturn(dto);

        mockMvc.perform(put("/api/v1/cardholders/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"updated@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("updated@example.com"));
    }

    @Test
    void shouldReturnNotFound_whenCardholderNotFound() throws Exception {
        when(cardholderService.getCardholderById(999L)).thenThrow(new RuntimeException("Cardholder not found"));

        mockMvc.perform(get("/api/v1/cardholders/999"))
                .andExpect(status().isNotFound());
    }
}