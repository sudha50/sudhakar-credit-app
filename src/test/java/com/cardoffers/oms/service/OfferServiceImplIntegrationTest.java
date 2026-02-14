package com.cardoffers.oms.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.containers.PostgreSQLContainer;
import org.springframework.boot.testcontainers.junit.jupiter.Container;
import org.springframework.boot.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import com.cardoffers.oms.exception.InvalidOfferException;
import com.cardoffers.oms.exception.ResourceNotFoundException;
import com.cardoffers.oms.model.dto.OfferDTO;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class OfferServiceImplIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> dbContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", dbContainer::getJdbcUrl);
        registry.add("spring.datasource.username", dbContainer::getUsername);
        registry.add("spring.datasource.password", dbContainer::getPassword);
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testCreateOffer() {
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setTitle("Special Offer");
        offerDTO.setDescription("A special discount on items.");
        offerDTO.setMerchant(new MerchantDTO(1L)); // Assuming merchant 1 exists
        offerDTO.setCardNetwork(new CardNetworkDTO(1L)); // Assuming card network 1 exists
        offerDTO.setStartDate(LocalDate.now());
        offerDTO.setEndDate(LocalDate.now().plusDays(10));
        offerDTO.setDiscountPercentage(20);
        offerDTO.setCashbackAmount(5);
        offerDTO.setMinimumPurchaseAmount(50);
        offerDTO.setMaxRedemptions(100);
        offerDTO.setActive(true);

        ResponseEntity<OfferDTO> response = restTemplate.postForEntity("/offers", offerDTO, OfferDTO.class);
        
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Special Offer", response.getBody().getTitle());
    }

    @Test
    public void testGetOfferByIdNotFound() {
        ResponseEntity<OfferDTO> response = restTemplate.getForEntity("/offers/999", OfferDTO.class);
        
        assertEquals(404, response.getStatusCodeValue());
        assertTrue(response.getBody() == null);
    }

    @Test
    public void testInvalidOfferDates() {
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setTitle("Invalid Offer");
        offerDTO.setDescription("This offer has invalid dates.");
        offerDTO.setMerchant(new MerchantDTO(1L)); // Assuming merchant 1 exists
        offerDTO.setCardNetwork(new CardNetworkDTO(1L)); // Assuming card network 1 exists
        offerDTO.setStartDate(LocalDate.now().plusDays(10));
        offerDTO.setEndDate(LocalDate.now());
        offerDTO.setDiscountPercentage(10);
        offerDTO.setActive(true);

        Exception exception = assertThrows(InvalidOfferException.class, () -> {
            restTemplate.postForEntity("/offers", offerDTO, OfferDTO.class);
        });

        String expectedMessage = "Offer endDate must be on/after startDate";
        String actualMessage = exception.getMessage();
        
        assertTrue(actualMessage.contains(expectedMessage));
    }
}