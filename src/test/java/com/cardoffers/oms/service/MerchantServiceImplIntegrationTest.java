package com.cardoffers.oms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.cardoffers.oms.exception.ResourceNotFoundException;
import com.cardoffers.oms.model.dto.MerchantDTO;
import com.cardoffers.oms.model.entity.Merchant;
import com.cardoffers.oms.repository.MerchantRepository;
import com.cardoffers.oms.mapper.MerchantMapper;

@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class MerchantServiceImplIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

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

    @BeforeEach
    void setUp() {
        merchantRepository.deleteAll();
    }

    @Test
    void testCreateMerchant() {
        MerchantDTO dto = new MerchantDTO();
        dto.setName("Test Merchant");
        dto.setDescription("Test Description");
        dto.setCategory("Test Category");
        dto.setLogoUrl("http://test.com/logo.png");
        dto.setWebsite("http://test.com");
        dto.setActive(true);

        ResponseEntity<MerchantDTO> response = restTemplate.postForEntity("/merchants", dto, MerchantDTO.class);
        
        assertEquals(201, response.getStatusCodeValue());
        MerchantDTO createdMerchant = response.getBody();
        assertNotNull(createdMerchant);
        assertEquals("Test Merchant", createdMerchant.getName());
    }

    @Test
    void testGetMerchantById() {
        Merchant merchant = new Merchant();
        merchant.setName("Sample Merchant");
        merchant.setDescription("Sample Description");
        merchant.setCategory("Sample Category");
        merchant.setLogoUrl("http://sample.com/logo.png");
        merchant.setWebsite("http://sample.com");
        merchant.setActive(true);
        merchantRepository.save(merchant);

        ResponseEntity<MerchantDTO> response = restTemplate.getForEntity("/merchants/" + merchant.getId(), MerchantDTO.class);
        
        assertEquals(200, response.getStatusCodeValue());
        MerchantDTO retrievedMerchant = response.getBody();
        assertNotNull(retrievedMerchant);
        assertEquals("Sample Merchant", retrievedMerchant.getName());
    }

    @Test
    void testGetMerchantById_NotFound() {
        ResponseEntity<MerchantDTO> response = restTemplate.getForEntity("/merchants/999", MerchantDTO.class);
        
        assertEquals(404, response.getStatusCodeValue());
        assertTrue(response.getBody() == null);
    }

    @Test
    void testUpdateMerchant() {
        Merchant merchant = new Merchant();
        merchant.setName("Old Merchant");
        merchant.setDescription("Old Description");
        merchant.setCategory("Old Category");
        merchant.setLogoUrl("http://old.com/logo.png");
        merchant.setWebsite("http://old.com");
        merchant.setActive(true);
        Merchant savedMerchant = merchantRepository.save(merchant);

        MerchantDTO updateDto = new MerchantDTO();
        updateDto.setName("Updated Merchant");
        updateDto.setDescription("Updated Description");
        updateDto.setCategory("Updated Category");
        updateDto.setLogoUrl("http://updated.com/logo.png");
        updateDto.setWebsite("http://updated.com");
        updateDto.setActive(false);

        restTemplate.put("/merchants/" + savedMerchant.getId(), updateDto);

        Optional<Merchant> updatedMerchantOpt = merchantRepository.findById(savedMerchant.getId());
        assertTrue(updatedMerchantOpt.isPresent());
        assertEquals("Updated Merchant", updatedMerchantOpt.get().getName());
        assertFalse(updatedMerchantOpt.get().getActive());
    }
}