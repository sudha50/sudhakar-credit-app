package com.cardoffers.oms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.jdbc.ContainerDisabledJdbc;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.ResponseEntity;

import io.r2dbc.spi.ConnectionFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class OfferEligibilityServiceImplIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OfferEligibilityServiceImpl offerEligibilityService;

    @Test
    public void testGetEligibleOffersForCardholder_HappyPath() throws Exception {
        // Setup test data here (add entries to your Cardholder and Offer repositories)
        Long cardholderId = 1L; // this would be the ID of an existing cardholder
        // Assume necessary data setup is done
        ResponseEntity<EligibleOfferResponseDTO> response = 
            mockMvc.perform(get("/eligibleOffers/" + cardholderId))
            .andExpect(status().isOk())
            .andReturn().getResponse();

        assertNotNull(response.getBody());
        assertEquals(cardholderId, response.getBody().getCardholderId());
        assertTrue(response.getBody().getTotalOffers() > 0);
    }

    @Test
    public void testGetEligibleOffersForCardholder_NoActiveCards() throws Exception {
        Long cardholderId = 2L; // assuming there's a cardholder with no active cards
        
        mockMvc.perform(get("/eligibleOffers/" + cardholderId))
            .andExpect(status().isForbidden()); // should return 403 based on Business Logic
    }

    @Test
    public void testFilterOffersByCategory_ValidCategory() throws Exception {
        Long cardholderId = 1L; // Assuming this cardholder exists
        String category = "Retail"; // Assuming this category exists in offers
        
        // Add offers that belong to the "Retail" category in setup
        
        ResponseEntity<List<OfferDTO>> response = 
            mockMvc.perform(get("/eligibleOffers/" + cardholderId + "/category/" + category))
            .andExpect(status().isOk())
            .andReturn().getResponse();

        assertNotNull(response.getBody());
        response.getBody().forEach(offer -> assertEquals(category.toUpperCase(), offer.getMerchant().getCategory().trim().toUpperCase()));
    }

    @Test
    public void testFilterOffersByCategory_EmptyResults() throws Exception {
        Long cardholderId = 1L; // assuming this cardholder exists
        String category = "NonExistentCategory"; // a non-existing category
        
        ResponseEntity<List<OfferDTO>> response = 
            mockMvc.perform(get("/eligibleOffers/" + cardholderId + "/category/" + category))
            .andExpect(status().isOk())
            .andReturn().getResponse();

        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }
}