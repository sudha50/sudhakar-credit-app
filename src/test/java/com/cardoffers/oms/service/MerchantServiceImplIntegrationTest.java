package com.cardoffers.oms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.Testcontainers;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.cardoffers.oms.exception.ResourceNotFoundException;
import com.cardoffers.oms.mapper.MerchantMapper;
import com.cardoffers.oms.model.dto.MerchantDTO;
import com.cardoffers.oms.model.entity.Merchant;
import com.cardoffers.oms.repository.MerchantRepository;

import org.testcontainers.containers.PostgreSQLContainer;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
public class MerchantServiceImplIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private MerchantMapper merchantMapper;

    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    static {
        postgresContainer.start();
    }

    @DynamicPropertySource
    static void postgresProperties(org.springframework.test.context.DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Test
    @Transactional
    public void testCreateMerchant() throws Exception {
        MerchantDTO dto = new MerchantDTO();
        dto.setName("Test Merchant");
        dto.setDescription("Test Description");
        dto.setCategory("Test Category");
        dto.setLogoUrl("http://test.com/logo.png");
        dto.setWebsite("http://test.com");
        dto.setActive(true);

        mockMvc.perform(post("/merchants")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Test Merchant\",\"description\":\"Test Description\",\"category\":\"Test Category\",\"logoUrl\":\"http://test.com/logo.png\",\"website\":\"http://test.com\",\"active\":true}"))
                .andExpect(status().isCreated());

        List<Merchant> merchants = merchantRepository.findByActiveTrue();
        assertEquals(1, merchants.size());
        assertEquals("Test Merchant", merchants.get(0).getName());
    }

    @Test
    @Transactional
    public void testGetMerchantById_HappyPath() throws Exception {
        Merchant merchant = new Merchant();
        merchant.setName("Existing Merchant");
        merchant.setDescription("An existing merchant");
        merchant.setCategory("Retail");
        merchant.setLogoUrl("http://existing.com/logo.png");
        merchant.setWebsite("http://existing.com");
        merchant.setActive(true);
        merchantRepository.save(merchant);

        mockMvc.perform(get("/merchants/{id}", merchant.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Existing Merchant"));
    }

    @Test
    public void testGetMerchantById_NotFound() throws Exception {
        mockMvc.perform(get("/merchants/{id}", 99999))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("Merchant not found", result.getResolvedException().getMessage()));
    }

    @Test
    @Transactional
    public void testUpdateMerchant() throws Exception {
        Merchant merchant = new Merchant();
        merchant.setName("Merchant To Update");
        merchant.setDescription("Old Description");
        merchant.setCategory("Food");
        merchant.setLogoUrl("http://old.com/logo.png");
        merchant.setWebsite("http://old.com");
        merchant.setActive(true);
        Merchant savedMerchant = merchantRepository.save(merchant);

        mockMvc.perform(put("/merchants/{id}", savedMerchant.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Merchant\",\"description\":\"Updated Description\",\"category\":\"Food\",\"logoUrl\":\"http://updated.com/logo.png\",\"website\":\"http://updated.com\",\"active\":false}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Merchant"));

        Optional<Merchant> updatedMerchant = merchantRepository.findById(savedMerchant.getId());
        assertTrue(updatedMerchant.isPresent());
        assertEquals("Updated Merchant", updatedMerchant.get().getName());
        assertFalse(updatedMerchant.get().getActive());
    }
}