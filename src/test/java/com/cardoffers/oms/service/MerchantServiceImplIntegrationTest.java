package com.cardoffers.oms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.containers.PostgreSQLContainer;
import org.springframework.boot.testcontainers.extension.Testcontainers;
import org.springframework.boot.test.context.DynamicPropertyRegistry;
import org.springframework.boot.test.context.DynamicPropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import com.cardoffers.oms.exception.ResourceNotFoundException;
import com.cardoffers.oms.model.dto.MerchantDTO;
import com.cardoffers.oms.model.entity.Merchant;
import com.cardoffers.oms.repository.MerchantRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MerchantServiceImplIntegrationTest {

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private RestTemplate restTemplate;

    static PostgreSQLContainer<?> databaseContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("password");

    static {
        databaseContainer.start();
    }

    @DynamicPropertySource
    static void dbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", databaseContainer::getJdbcUrl);
        registry.add("spring.datasource.username", databaseContainer::getUsername);
        registry.add("spring.datasource.password", databaseContainer::getPassword);
    }

    @Test
    void testCreateMerchant() {
        MerchantDTO newMerchant = new MerchantDTO();
        newMerchant.setName("Merchant Test");
        newMerchant.setDescription("Test description");
        newMerchant.setCategory("Test Category");
        newMerchant.setLogoUrl("http://example.com/logo.png");
        newMerchant.setWebsite("http://example.com");
        newMerchant.setActive(true);

        ResponseEntity<MerchantDTO> response = restTemplate.postForEntity("/merchants", newMerchant, MerchantDTO.class);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Merchant Test", response.getBody().getName());
    }

    @Test
    void testGetMerchantById() {
        Merchant merchant = new Merchant();
        merchant.setName("Existing Merchant");
        merchant.setDescription("Description");
        merchant.setCategory("Category");
        merchant.setLogoUrl("http://example.com/logo.png");
        merchant.setWebsite("http://example.com");
        merchant.setActive(true);
        merchantRepository.save(merchant);

        ResponseEntity<MerchantDTO> response = restTemplate.getForEntity("/merchants/" + merchant.getId(), MerchantDTO.class);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Existing Merchant", response.getBody().getName());
    }

    @Test
    void testGetMerchantById_NotFound() {
        ResponseEntity<MerchantDTO> response = restTemplate.getForEntity("/merchants/9999", MerchantDTO.class);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testGetMerchantsByCategory() {
        Merchant merchant = new Merchant();
        merchant.setName("Category Merchant");
        merchant.setDescription("Category description");
        merchant.setCategory("TestCategory");
        merchant.setLogoUrl("http://example.com/logo.png");
        merchant.setWebsite("http://example.com");
        merchant.setActive(true);
        merchantRepository.save(merchant);

        List<MerchantDTO> merchants = restTemplate.getForObject("/merchants/category/TestCategory", List.class);

        assertNotNull(merchants);
        assertFalse(merchants.isEmpty());
        assertEquals(1, merchants.size());
        assertEquals("Category Merchant", merchants.get(0).getName());
    }
}