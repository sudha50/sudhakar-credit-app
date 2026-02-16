package com.cardoffers.oms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.container.Testcontainers;
import org.springframework.boot.test.util.DynamicPropertyRegistry;
import org.springframework.boot.test.util.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.TestRestTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.cardoffers.oms.exception.ResourceNotFoundException;
import com.cardoffers.oms.model.dto.MerchantDTO;
import com.cardoffers.oms.model.entity.Merchant;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MerchantServiceImplIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testCreateMerchant() {
        MerchantDTO merchantDTO = new MerchantDTO();
        merchantDTO.setName("Test Merchant");
        merchantDTO.setDescription("A description for test merchant");
        merchantDTO.setCategory("Test Category");
        merchantDTO.setLogoUrl("http://example.com/logo.png");
        merchantDTO.setWebsite("http://example.com");
        merchantDTO.setActive(true);

        MerchantDTO response = restTemplate.postForObject("/merchants", merchantDTO, MerchantDTO.class);

        assertNotNull(response);
        assertEquals("Test Merchant", response.getName());
        assertNotNull(response.getId()); // Assuming ID is generated and returned
    }

    @Test
    public void testGetMerchantById() {
        // Assuming there's a merchant with ID 1
        MerchantDTO merchantDTO = restTemplate.getForObject("/merchants/1", MerchantDTO.class);

        assertNotNull(merchantDTO);
        assertEquals(1L, merchantDTO.getId());
    }

    @Test
    public void testGetMerchantsByCategory_NotFound() {
        String nonExistingCategory = "NonExistingCategory";
        List<MerchantDTO> merchants = restTemplate.getForObject("/merchants/category/" + nonExistingCategory, List.class);

        assertNotNull(merchants);
        assertTrue(merchants.isEmpty());
    }

    @Test
    public void testUpdateMerchant() {
        // Assuming that merchant with ID 1 exists
        MerchantDTO updateDTO = new MerchantDTO();
        updateDTO.setName("Updated Merchant");
        updateDTO.setDescription("Updated description for merchant");
        updateDTO.setCategory("Updated Category");
        updateDTO.setLogoUrl("http://example.com/updated_logo.png");
        updateDTO.setWebsite("http://example.com/updated");
        updateDTO.setActive(true);

        MerchantDTO updatedMerchant = restTemplate.exchange("/merchants/1", HttpMethod.PUT, new HttpEntity<>(updateDTO), MerchantDTO.class).getBody();

        assertNotNull(updatedMerchant);
        assertEquals("Updated Merchant", updatedMerchant.getName());
    }

}