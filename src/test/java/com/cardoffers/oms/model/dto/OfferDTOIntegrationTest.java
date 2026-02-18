package com.cardoffers.oms.model.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OfferDTOIntegrationTest {

    @Autowired
    private RestTemplate restTemplate;

    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    static {
        postgresContainer.start();
    }

    @DynamicPropertySource
    static void configureDatabase() {
        System.setProperty("spring.datasource.url", postgresContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgresContainer.getUsername());
        System.setProperty("spring.datasource.password", postgresContainer.getPassword());
    }

    @Test
    void testCreateOffer() {
        MerchantDTO merchant = new MerchantDTO();
        merchant.setId(1L);

        CardNetworkDTO cardNetwork = new CardNetworkDTO();
        cardNetwork.setId(1L);

        OfferDTO offer = new OfferDTO();
        offer.setTitle("Summer Sale");
        offer.setOfferType("Discount");
        offer.setDiscountPercentage(new BigDecimal("10.00"));
        offer.setCashbackAmount(new BigDecimal("5.00"));
        offer.setMinimumPurchaseAmount(new BigDecimal("50.00"));
        offer.setStartDate(LocalDate.now());
        offer.setEndDate(LocalDate.now().plusDays(10));
        offer.setMerchant(merchant);
        offer.setCardNetwork(cardNetwork);
        offer.setSource("Web");
        offer.setMaxRedemptions(100);
        offer.setCurrentRedemptions(0);
        offer.setActive(true);

        ResponseEntity<OfferDTO> response = restTemplate.postForEntity("/api/offers", offer, OfferDTO.class);
        
        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody().getId());
    }

    @Test
    void testCreateOfferWithMissingFields() {
        OfferDTO offer = new OfferDTO();
        
        ResponseEntity<String> response = restTemplate.postForEntity("/api/offers", offer, String.class);
        
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("must not be blank"));
    }

    @Test
    void testRetrieveOfferById() {
        MerchantDTO merchant = new MerchantDTO();
        merchant.setId(1L);

        CardNetworkDTO cardNetwork = new CardNetworkDTO();
        cardNetwork.setId(1L);

        OfferDTO offer = new OfferDTO();
        offer.setTitle("Fall Sale");
        offer.setOfferType("Cashback");
        offer.setDiscountPercentage(new BigDecimal("15.00"));
        offer.setCashbackAmount(new BigDecimal("10.00"));
        offer.setMinimumPurchaseAmount(new BigDecimal("75.00"));
        offer.setStartDate(LocalDate.now());
        offer.setEndDate(LocalDate.now().plusDays(15));
        offer.setMerchant(merchant);
        offer.setCardNetwork(cardNetwork);
        offer.setSource("App");
        offer.setMaxRedemptions(200);
        offer.setCurrentRedemptions(50);
        offer.setActive(true);

        ResponseEntity<OfferDTO> createdOfferResponse = restTemplate.postForEntity("/api/offers", offer, OfferDTO.class);
        Long offerId = createdOfferResponse.getBody().getId();

        ResponseEntity<OfferDTO> retrievedOfferResponse = restTemplate.getForEntity("/api/offers/" + offerId, OfferDTO.class);
        
        assertEquals(200, retrievedOfferResponse.getStatusCodeValue());
        assertEquals("Fall Sale", retrievedOfferResponse.getBody().getTitle());
    }
}