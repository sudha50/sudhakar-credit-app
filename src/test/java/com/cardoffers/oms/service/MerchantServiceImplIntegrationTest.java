package com.cardoffers.oms.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.containers.PostgreSQLContainer;
import org.springframework.boot.testcontainers.utility.Duration;
import org.springframework.boot.testcontainers.utility.SocketUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.testcontainers.containers.Container;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.cardoffers.oms.model.dto.MerchantDTO;
import com.cardoffers.oms.repository.MerchantRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MerchantServiceImplIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("password")
            .withStartupTimeout(Duration.ofSeconds(60));
    
    @DynamicPropertySource
    static void propertySource(org.springframework.test.context.DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MerchantRepository merchantRepository;

    @Test
    public void createMerchant_ShouldReturnMerchant() {
        MerchantDTO merchant = new MerchantDTO();
        merchant.setName("Test Merchant");
        merchant.setDescription("A description");
        merchant.setCategory("Retail");
        merchant.setLogoUrl("http://example.com/logo.png");
        merchant.setWebsite("http://example.com");
        merchant.setActive(true);

        ResponseEntity<MerchantDTO> response = restTemplate.postForEntity("/merchants", merchant, MerchantDTO.class);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Merchant", response.getBody().getName());
    }

    @Test
    public void getMerchantById_ShouldReturnMerchant() {
        MerchantDTO newMerchant = new MerchantDTO();
        newMerchant.setName("Another Merchant");
        newMerchant.setDescription("Another description");
        newMerchant.setCategory("Food");
        newMerchant.setLogoUrl("http://example.com/anotherlogo.png");
        newMerchant.setWebsite("http://anotherexample.com");
        newMerchant.setActive(true);

        MerchantDTO createdMerchant = restTemplate.postForObject("/merchants", newMerchant, MerchantDTO.class);
        Long createdId = createdMerchant.getId();

        ResponseEntity<MerchantDTO> response = restTemplate.getForEntity("/merchants/" + createdId, MerchantDTO.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Another Merchant", response.getBody().getName());
    }

    @Test
    public void getMerchantById_ShouldReturnNotFound() {
        Long invalidId = 999L;

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            restTemplate.getForEntity("/merchants/" + invalidId, MerchantDTO.class);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Merchant not found", exception.getReason());
    }

    @Test
    public void getMerchantsByCategory_ShouldReturnActiveMerchants() {
        MerchantDTO merchant1 = new MerchantDTO();
        merchant1.setName("Category Merchant 1");
        merchant1.setDescription("Category description 1");
        merchant1.setCategory("Books");
        merchant1.setLogoUrl("http://example.com/booklogo.png");
        merchant1.setWebsite("http://bookexample.com");
        merchant1.setActive(true);
        
        MerchantDTO merchant2 = new MerchantDTO();
        merchant2.setName("Category Merchant 2");
        merchant2.setDescription("Category description 2");
        merchant2.setCategory("Books");
        merchant2.setLogoUrl("http://example.com/booklogo2.png");
        merchant2.setWebsite("http://bookexample2.com");
        merchant2.setActive(false);

        restTemplate.postForObject("/merchants", merchant1, MerchantDTO.class);
        restTemplate.postForObject("/merchants", merchant2, MerchantDTO.class);

        ResponseEntity<List> response = restTemplate.getForEntity("/merchants/category/Books", List.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Category Merchant 1", ((MerchantDTO)response.getBody().get(0)).getName());
    }
}