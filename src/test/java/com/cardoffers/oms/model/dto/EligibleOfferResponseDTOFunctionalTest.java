package com.cardoffers.oms.model.dto;

import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(EligibleOfferResponseDTO.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EligibleOfferResponseDTOFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void shouldReturnEligibleOfferResponse() throws Exception {
        EligibleOfferResponseDTO responseDTO = new EligibleOfferResponseDTO();
        responseDTO.setCardholderId(1L);
        responseDTO.setCardholderName("John Doe");
        responseDTO.setTotalOffers(2);

        mockMvc.perform(post("/eligible-offers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(responseDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardholderId").value(1L))
                .andExpect(jsonPath("$.cardholderName").value("John Doe"))
                .andExpect(jsonPath("$.totalOffers").value(2));
    }

    @Test
    public void shouldRejectWhenCardholderNameIsNull() throws Exception {
        EligibleOfferResponseDTO responseDTO = new EligibleOfferResponseDTO();
        responseDTO.setCardholderId(1L);
        responseDTO.setTotalOffers(2);

        mockMvc.perform(post("/eligible-offers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(responseDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldRejectWhenTotalOffersIsNull() throws Exception {
        EligibleOfferResponseDTO responseDTO = new EligibleOfferResponseDTO();
        responseDTO.setCardholderId(1L);
        responseDTO.setCardholderName("John Doe");

        mockMvc.perform(post("/eligible-offers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(responseDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldHandleEmptyEligibleOffers() throws Exception {
        EligibleOfferResponseDTO responseDTO = new EligibleOfferResponseDTO();
        responseDTO.setCardholderId(1L);
        responseDTO.setCardholderName("John Doe");
        responseDTO.setTotalOffers(0);

        mockMvc.perform(post("/eligible-offers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(responseDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eligibleOffers").isEmpty());
    }

    @Test
    public void shouldReturnNotFoundForInvalidCardholderId() throws Exception {
        // Assuming there’s a service method that handles invalid cardholder IDs
        mockMvc.perform(get("/eligible-offers/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}