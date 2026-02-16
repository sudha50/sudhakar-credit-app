package com.cardoffers.oms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicPropertyRegistry;
import org.junit.jupiter.api.DynamicPropertySource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.cardoffers.oms.exception.ResourceNotFoundException;
import com.cardoffers.oms.mapper.MerchantMapper;
import com.cardoffers.oms.model.dto.MerchantDTO;
import com.cardoffers.oms.model.entity.Merchant;
import com.cardoffers.oms.repository.MerchantRepository;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MerchantServiceImplIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test_db")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void injectProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private MerchantMapper merchantMapper;

    @BeforeEach
    void setUp() {
        merchantRepository.deleteAll();
    }

    @Test
    void testCreateMerchant() throws Exception {
        MerchantDTO merchantDTO = new MerchantDTO();
        merchantDTO.setName("Test Merchant");
        merchantDTO.setDescription("Merchant description");
        merchantDTO.setCategory("Electronics");
        merchantDTO.setLogoUrl("http://example.com/logo.png");
        merchantDTO.setWebsite("http://example.com");
        merchantDTO.setActive(true);

        MvcResult result = mockMvc.perform(post("/merchants")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Test Merchant\",\"description\":\"Merchant description\",\"category\":\"Electronics\",\"logoUrl\":\"http://example.com/logo.png\",\"website\":\"http://example.com\",\"active\":true}"))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertNotNull(responseBody);
        assertTrue(responseBody.contains("Test Merchant"));
        assertEquals(1, merchantRepository.count());
    }

    @Test
    void testGetMerchantById_HappyPath() throws Exception {
        Merchant merchant = new Merchant();
        merchant.setName("Sample Merchant");
        merchant.setDescription("Sample Description");
        merchant.setCategory("Food");
        merchant.setLogoUrl("http://sample.com/logo.png");
        merchant.setWebsite("http://sample.com");
        merchant.setActive(true);
        merchant = merchantRepository.save(merchant);

        MvcResult result = mockMvc.perform(get("/merchants/{id}", merchant.getId()))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertNotNull(responseBody);
        assertTrue(responseBody.contains("Sample Merchant"));
    }

    @Test
    void testGetMerchantById_NotFound() throws Exception {
        mockMvc.perform(get("/merchants/{id}", 9999L)) // Non-existent ID
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException));
    }

    @Test
    void testGetMerchantsByCategory() throws Exception {
        Merchant merchant = new Merchant();
        merchant.setName("Category Merchant");
        merchant.setDescription("Description");
        merchant.setCategory("Toys");
        merchant.setLogoUrl("http://category.com/logo.png");
        merchant.setWebsite("http://category.com");
        merchant.setActive(true);
        merchantRepository.save(merchant);

        MvcResult result = mockMvc.perform(get("/merchants/category/Toys"))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertTrue(responseBody.contains("Category Merchant"));
    }
}