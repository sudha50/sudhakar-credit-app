package com.cardoffers.oms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc.*;
import static org.springframework.test.context.DynamicPropertyRegistry.*;
import static org.springframework.test.context.DynamicPropertySource.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.web.servlet;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.containers.PostgreSQLContainer;
import org.springframework.boot.testcontainers.lifecycle.Startable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import com.cardoffers.oms.mapper.MerchantMapper;
import com.cardoffers.oms.model.dto.MerchantDTO;
import com.cardoffers.oms.model.entity.Merchant;
import com.cardoffers.oms.repository.MerchantRepository;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
public class MerchantServiceImplIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test_db")
            .withUsername("user")
            .withPassword("password");

    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }
    
    @Autowired
    private MerchantServiceImpl merchantService;

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private MerchantMapper merchantMapper;

    @Test
    void testCreateMerchant() {
        MerchantDTO dto = new MerchantDTO();
        dto.setName("Test Merchant");
        dto.setDescription("A merchant for testing");
        dto.setCategory("Food");
        dto.setLogoUrl("http://example.com/logo.png");
        dto.setWebsite("http://example.com");
        dto.setActive(true);

        MerchantDTO createdMerchant = merchantService.createMerchant(dto);

        assertNotNull(createdMerchant);
        assertEquals("Test Merchant", createdMerchant.getName());
        assertEquals("A merchant for testing", createdMerchant.getDescription());
    }

    @Test
    void testGetMerchantById() {
        Merchant merchant = new Merchant();
        merchant.setName("Existing Merchant");
        merchant.setDescription("An existing merchant");
        merchant.setCategory("Retail");
        merchant.setLogoUrl("http://example.com/logo_existing.png");
        merchant.setWebsite("http://example_existing.com");
        merchant.setActive(true);
        merchantRepository.save(merchant);

        MerchantDTO foundMerchant = merchantService.getMerchantById(merchant.getId());

        assertNotNull(foundMerchant);
        assertEquals("Existing Merchant", foundMerchant.getName());
    }

    @Test
    void testGetMerchantsByCategory() {
        Merchant merchant1 = new Merchant();
        merchant1.setName("Food Merchant 1");
        merchant1.setDescription("First food merchant");
        merchant1.setCategory("Food");
        merchant1.setLogoUrl("http://example.com/logo_food1.png");
        merchant1.setWebsite("http://example_food1.com");
        merchant1.setActive(true);
        merchantRepository.save(merchant1);
        
        Merchant merchant2 = new Merchant();
        merchant2.setName("Food Merchant 2");
        merchant2.setDescription("Second food merchant");
        merchant2.setCategory("Food");
        merchant2.setLogoUrl("http://example.com/logo_food2.png");
        merchant2.setWebsite("http://example_food2.com");
        merchant2.setActive(true);
        merchantRepository.save(merchant2);

        List<MerchantDTO> foodMerchants = merchantService.getMerchantsByCategory("Food");

        assertEquals(2, foodMerchants.size());
    }

    @Test
    void testUpdateMerchant() {
        Merchant merchant = new Merchant();
        merchant.setName("Update Merchant");
        merchant.setDescription("Merchant to update");
        merchant.setCategory("Tech");
        merchant.setLogoUrl("http://example.com/logo_update.png");
        merchant.setWebsite("http://example_update.com");
        merchant.setActive(true);
        Merchant savedMerchant = merchantRepository.save(merchant);

        MerchantDTO updatedData = new MerchantDTO();
        updatedData.setName("Updated Merchant");
        updatedData.setDescription("Updated description");
        updatedData.setCategory("Tech");
        updatedData.setLogoUrl("http://example.com/logo_updated.png");
        updatedData.setWebsite("http://updated_example.com");
        updatedData.setActive(false);

        MerchantDTO updatedMerchant = merchantService.updateMerchant(savedMerchant.getId(), updatedData);

        assertNotNull(updatedMerchant);
        assertEquals("Updated Merchant", updatedMerchant.getName());
        assertFalse(updatedMerchant.getActive());
    }
}