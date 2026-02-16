package com.cardoffers.oms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import com.cardoffers.oms.model.dto.MerchantDTO;
import com.cardoffers.oms.model.entity.Merchant;
import com.cardoffers.oms.repository.MerchantRepository;
import com.cardoffers.oms.mapper.MerchantMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class MerchantServiceImplIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15.1")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("password");

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

    @Autowired
    private MerchantMapper merchantMapper;

    @Test
    public void testCreateMerchant() {
        MerchantDTO dto = new MerchantDTO();
        dto.setName("Test Merchant");
        dto.setDescription("Test Description");
        dto.setCategory("Test Category");
        dto.setLogoUrl("http://example.com/logo.png");
        dto.setWebsite("http://example.com");
        dto.setActive(true);

        ResponseEntity<MerchantDTO> response = restTemplate.postForEntity("/merchants", dto, MerchantDTO.class);
        
        assertEquals(CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Merchant", response.getBody().getName());
        assertEquals("Test Category", response.getBody().getCategory());
    }

    @Test
    public void testGetMerchantById() {
        Merchant merchant = new Merchant();
        merchant.setName("Test Merchant");
        merchant.setDescription("Test Description");
        merchant.setCategory("Test Category");
        merchant.setLogoUrl("http://example.com/logo.png");
        merchant.setWebsite("http://example.com");
        merchant.setActive(true);
        merchant = merchantRepository.save(merchant);

        ResponseEntity<MerchantDTO> response = restTemplate.getForEntity("/merchants/" + merchant.getId(), MerchantDTO.class);
        
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(merchant.getId(), response.getBody().getId());
        assertEquals("Test Merchant", response.getBody().getName());
    }

    @Test
    public void testGetMerchantNotFound() {
        ResponseEntity<MerchantDTO> response = restTemplate.getForEntity("/merchants/9999", MerchantDTO.class);
        
        assertEquals(NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetMerchantsByCategory() {
        Merchant merchant1 = new Merchant();
        merchant1.setName("Food Merchant");
        merchant1.setDescription("Delicious food");
        merchant1.setCategory("Food");
        merchant1.setLogoUrl("http://example.com/logo1.png");
        merchant1.setWebsite("http://food.com");
        merchant1.setActive(true);
        merchantRepository.save(merchant1);

        Merchant merchant2 = new Merchant();
        merchant2.setName("Tech Merchant");
        merchant2.setDescription("Latest gadgets");
        merchant2.setCategory("Technology");
        merchant2.setLogoUrl("http://example.com/logo2.png");
        merchant2.setWebsite("http://tech.com");
        merchant2.setActive(true);
        merchantRepository.save(merchant2);

        ResponseEntity<List> response = restTemplate.getForEntity("/merchants/category/Food", List.class);
        
        assertEquals(OK, response.getStatusCode());
        assertTrue(response.getBody().size() > 0);
        assertEquals("Food Merchant", ((Map) response.getBody().get(0)).get("name"));
    }
}