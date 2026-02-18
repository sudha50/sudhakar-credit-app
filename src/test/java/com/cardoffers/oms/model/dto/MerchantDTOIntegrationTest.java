package com.cardoffers.oms.model.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.test.web.client.TestRestTemplate;

@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = NONE)
@ActiveProfiles("test")
class MerchantDTOIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgresContainer = 
            new PostgreSQLContainer<>("postgres:latest")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void postgresProperties(org.springframework.test.context.DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testCreateMerchant() {
        MerchantDTO merchant = new MerchantDTO();
        merchant.setName("Test Merchant");
        merchant.setDescription("A description for test merchant");
        merchant.setCategory("Retail");
        merchant.setLogoUrl("http://example.com/logo.png");
        merchant.setWebsite("http://example.com");
        merchant.setActive(true);

        ResponseEntity<MerchantDTO> response = restTemplate.postForEntity("/merchants", merchant, MerchantDTO.class);
        
        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Test Merchant", response.getBody().getName());
    }

    @Test
    void testCreateMerchant_WithMissingName_ReturnsBadRequest() {
        MerchantDTO merchant = new MerchantDTO();
        merchant.setDescription("Merchant without a name");
        merchant.setCategory("Retail");

        ResponseEntity<MerchantDTO> response = restTemplate.postForEntity("/merchants", merchant, MerchantDTO.class);
        
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testGetMerchantById() {
        MerchantDTO merchant = new MerchantDTO();
        merchant.setName("Retrievable Merchant");
        merchant.setDescription("A merchant that can be retrieved");
        merchant.setCategory("Service");
        merchant.setActive(true);

        MerchantDTO createdMerchant = restTemplate.postForObject("/merchants", merchant, MerchantDTO.class);
        assertNotNull(createdMerchant);
        Long createdMerchantId = createdMerchant.getId();

        ResponseEntity<MerchantDTO> response = restTemplate.getForEntity("/merchants/" + createdMerchantId, MerchantDTO.class);
        
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(createdMerchantId, response.getBody().getId());
    }
}