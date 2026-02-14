package com.cardoffers.oms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.containers.PostgreSQLContainer;
import org.springframework.boot.testcontainers.utility.DockerImageName;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestTemplate;

import com.cardoffers.oms.model.dto.OfferDTO;
import com.cardoffers.oms.model.entity.CardNetwork;
import com.cardoffers.oms.model.entity.Merchant;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.testcontainers.Testcontainers;
import org.springframework.boot.test.web.client.TestRestTemplate;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class OfferServiceImplIntegrationTest {

    @Container
    public PostgreSQLContainer<?> dbContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("password");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", dbContainer::getJdbcUrl);
        registry.add("spring.datasource.username", dbContainer::getUsername);
        registry.add("spring.datasource.password", dbContainer::getPassword);
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testCreateOffer() {
        Merchant merchant = new Merchant();
        merchant.setId(1L);
        CardNetwork cardNetwork = new CardNetwork();
        cardNetwork.setId(1L);

        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setTitle("Special Discount");
        offerDTO.setDescription("Get 20% off on all items");
        offerDTO.setOfferType("DISCOUNT");
        offerDTO.setDiscountPercentage(20);
        offerDTO.setCashbackAmount(0);
        offerDTO.setMinimumPurchaseAmount(100);
        offerDTO.setStartDate(LocalDate.now());
        offerDTO.setEndDate(LocalDate.now().plusDays(10));
        offerDTO.setMerchant(merchant);
        offerDTO.setCardNetwork(cardNetwork);
        
        ResponseEntity<OfferDTO> response = restTemplate.postForEntity("/offers", offerDTO, OfferDTO.class);
        
        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Special Discount", response.getBody().getTitle());
    }

    @Test
    public void testCreateOfferWithInvalidDates() {
        Merchant merchant = new Merchant();
        merchant.setId(1L);
        CardNetwork cardNetwork = new CardNetwork();
        cardNetwork.setId(1L);

        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setTitle("Invalid Offer");
        offerDTO.setDescription("This offer has invalid dates");
        offerDTO.setOfferType("DISCOUNT");
        offerDTO.setDiscountPercentage(20);
        offerDTO.setCashbackAmount(0);
        offerDTO.setMinimumPurchaseAmount(100);
        offerDTO.setStartDate(LocalDate.now().plusDays(10));
        offerDTO.setEndDate(LocalDate.now());

        HttpEntity<OfferDTO> requestEntity = new HttpEntity<>(offerDTO);
        ResponseEntity<String> response = restTemplate.exchange("/offers", HttpMethod.POST, requestEntity, String.class);

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Offer endDate must be on/after startDate"));
    }

    @Test
    public void testGetOfferById() {
        Merchant merchant = new Merchant();
        merchant.setId(1L);
        CardNetwork cardNetwork = new CardNetwork();
        cardNetwork.setId(1L);

        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setTitle("New Year Offer");
        offerDTO.setDescription("Get additional cashback on purchases");
        offerDTO.setOfferType("CASHBACK");
        offerDTO.setDiscountPercentage(0);
        offerDTO.setCashbackAmount(50);
        offerDTO.setMinimumPurchaseAmount(100);
        offerDTO.setStartDate(LocalDate.now());
        offerDTO.setEndDate(LocalDate.now().plusDays(20));
        offerDTO.setMerchant(merchant);
        offerDTO.setCardNetwork(cardNetwork);
        
        ResponseEntity<OfferDTO> createdResponse = restTemplate.postForEntity("/offers", offerDTO, OfferDTO.class);
        assertEquals(201, createdResponse.getStatusCodeValue());
        
        Long createdOfferId = createdResponse.getBody().getId();
        ResponseEntity<OfferDTO> retrievedResponse = restTemplate.getForEntity("/offers/" + createdOfferId, OfferDTO.class);

        assertEquals(200, retrievedResponse.getStatusCodeValue());
        assertEquals("New Year Offer", retrievedResponse.getBody().getTitle());
    }

    @Test
    public void testGetOffersByMerchant() {
        Merchant merchant = new Merchant();
        merchant.setId(1L);
        CardNetwork cardNetwork = new CardNetwork();
        cardNetwork.setId(1L);

        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setTitle("Holiday Offer");
        offerDTO.setDescription("Discount during holidays");
        offerDTO.setOfferType("DISCOUNT");
        offerDTO.setDiscountPercentage(30);
        offerDTO.setCashbackAmount(0);
        offerDTO.setMinimumPurchaseAmount(150);
        offerDTO.setStartDate(LocalDate.now());
        offerDTO.setEndDate(LocalDate.now().plusDays(30));
        offerDTO.setMerchant(merchant);
        offerDTO.setCardNetwork(cardNetwork);
        
        restTemplate.postForEntity("/offers", offerDTO, OfferDTO.class);
        
        ResponseEntity<List> response = restTemplate.getForEntity("/offers/merchant/" + merchant.getId(), List.class);
        
        assertEquals(200, response.getStatusCodeValue());
        assertFalse(response.getBody().isEmpty());
    }
}