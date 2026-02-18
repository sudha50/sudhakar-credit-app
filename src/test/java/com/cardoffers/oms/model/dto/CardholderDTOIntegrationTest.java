package com.cardoffers.oms.model.dto;

import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@SpringBootTest
@ActiveProfiles("test")
public class CardholderDTOIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateCardholder_Success() throws Exception {
        String cardholderJson = "{ \"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"john.doe@example.com\", \"phoneNumber\": \"1234567890\", \"active\": true }";

        mockMvc.perform(post("/api/cardholders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(cardholderJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    public void testCreateCardholder_InvalidEmail() throws Exception {
        String cardholderJson = "{ \"firstName\": \"Jane\", \"lastName\": \"Doe\", \"email\": \"not-an-email\", \"phoneNumber\": \"0987654321\", \"active\": true }";

        mockMvc.perform(post("/api/cardholders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(cardholderJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("email must be a well-formed email address"));
    }

    @Test
    public void testCreateCardholder_MissingFirstName() throws Exception {
        String cardholderJson = "{ \"lastName\": \"Doe\", \"email\": \"jane.doe@example.com\", \"phoneNumber\": \"0987654321\", \"active\": true }";

        mockMvc.perform(post("/api/cardholders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(cardholderJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("firstName must not be blank"));
    }
}