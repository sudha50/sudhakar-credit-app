package com.cardoffers.oms.model.dto;

import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class EligibleOfferResponseDTOIntegrationTest {

    @Test
    void givenValidData_whenCreatingEligibleOfferResponse_thenReturnsSuccess() throws Exception {
        EligibleOfferResponseDTO offerResponse = new EligibleOfferResponseDTO();
        offerResponse.setCardholderId(1L);
        offerResponse.setCardholderName("John Doe");
        offerResponse.setEligibleOffers(Arrays.asList(new OfferDTO()));
        offerResponse.setTotalOffers(5);

        // Perform the request and validate the response
        // Assuming there's a specific endpoint you are testing, e.g. "/api/eligible-offers"
        mockMvc.perform(post("/api/eligible-offers")
                .contentType("application/json")
                .content(asJsonString(offerResponse)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardholderName").value("John Doe"))
                .andExpect(jsonPath("$.totalOffers").value(5));
    }

    @Test
    void givenInvalidData_whenCreatingEligibleOfferResponse_thenReturnsBadRequest() throws Exception {
        EligibleOfferResponseDTO offerResponse = new EligibleOfferResponseDTO();
        // Missing required fields to trigger validation errors

        mockMvc.perform(post("/api/eligible-offers")
                .contentType("application/json")
                .content(asJsonString(offerResponse)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenFetchingEligibleOffers_thenReturnsCorrectResponse() throws Exception {
        // Assuming the data is already persisted in the database
        mockMvc.perform(get("/api/eligible-offers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardholderId").value(1L))
                .andExpect(jsonPath("$.cardholderName").value("John Doe"));
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}