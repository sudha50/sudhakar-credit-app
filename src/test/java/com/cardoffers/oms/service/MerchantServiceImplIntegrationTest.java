package com.cardoffers.oms.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.Testcontainers;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.cardoffers.oms.exception.ResourceNotFoundException;
import com.cardoffers.oms.model.dto.MerchantDTO;
import com.cardoffers.oms.model.entity.Merchant;
import com.cardoffers.oms.repository.MerchantRepository;

@Testcontainers
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
class MerchantServiceImplIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpassword");

    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MerchantRepository merchantRepository;

    @Test
    void testCreateMerchant() {
        MerchantDTO merchantDTO = new MerchantDTO();
        merchantDTO.setName("Test Merchant");
        merchantDTO.setDescription("Merchant Description");
        merchantDTO.setCategory("Test Category");
        merchantDTO.setLogoUrl("http://example.com/logo.png");
        merchantDTO.setWebsite("http://example.com");
        merchantDTO.setActive(true);

        MerchantDTO response = restTemplate.postForObject("/merchants", merchantDTO, MerchantDTO.class);

        assertNotNull(response);
        assertEquals("Test Merchant", response.getName());
        assertTrue(response.getActive());
    }

    @Test
    void testGetMerchantById() {
        Merchant merchant = new Merchant();
        merchant.setName("Sample Merchant");
        merchant.setDescription("Sample Description");
        merchant.setCategory("Sample Category");
        merchant.setLogoUrl("http://example.com/logo.png");
        merchant.setWebsite("http://example.com");
        merchant.setActive(true);
        merchantRepository.save(merchant);

        MerchantDTO response = restTemplate.getForObject("/merchants/{id}", MerchantDTO.class, merchant.getId());

        assertNotNull(response);
        assertEquals(merchant.getName(), response.getName());
    }

    @Test
    void testGetMerchantById_NotFound() {
        Long nonExistentId = 999L;
        var exception = assertThrows(ResourceNotFoundException.class, () -> {
            restTemplate.getForObject("/merchants/{id}", MerchantDTO.class, nonExistentId);
        });

        assertEquals("Merchant not found", exception.getMessage());
    }

    @Test
    void testGetMerchantsByCategory() {
        Merchant merchant = new Merchant();
        merchant.setName("Category Merchant");
        merchant.setDescription("Category Description");
        merchant.setCategory("Test Category");
        merchant.setLogoUrl("http://example.com/logo.png");
        merchant.setWebsite("http://example.com");
        merchant.setActive(true);
        merchantRepository.save(merchant);

        List<MerchantDTO> response = restTemplate.getForObject("/merchants/category/{category}", List.class, "Test Category");

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals("Category Merchant", ((MerchantDTO) response.get(0)).getName());
    }
}