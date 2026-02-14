package com.cardoffers.oms.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
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
import org.springframework.boot.testcontainers.dao.DynamicPropertySource;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.testcontainers.junit.jupiter.Container;
import org.springframework.boot.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.web.servlet.MockMvc;

import com.cardoffers.oms.exception.ResourceNotFoundException;
import com.cardoffers.oms.model.dto.MerchantDTO;
import com.cardoffers.oms.model.entity.Merchant;
import com.cardoffers.oms.repository.MerchantRepository;
import org.testcontainers.containers.PostgreSQLContainer;

@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MerchantServiceImplIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
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
    private MerchantRepository merchantRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateMerchant() throws Exception {
        MerchantDTO dto = new MerchantDTO();
        dto.setName("Test Merchant");
        dto.setDescription("A merchant for testing");
        dto.setCategory("Test Category");
        dto.setLogoUrl("http://example.com/logo.png");
        dto.setWebsite("http://example.com");
        dto.setActive(true);

        ResponseEntity<MerchantDTO> response = mockMvc.perform(post("/merchants")
                .contentType("application/json")
                .content("{\"name\":\"Test Merchant\",\"description\":\"A merchant for testing\",\"category\":\"Test Category\",\"logoUrl\":\"http://example.com/logo.png\",\"website\":\"http://example.com\",\"active\":true}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse();

        MerchantDTO createdMerchant = response.getBody();
        assertNotNull(createdMerchant);
        assertEquals("Test Merchant", createdMerchant.getName());
        assertEquals("A merchant for testing", createdMerchant.getDescription());
    }

    @Test
    public void testGetMerchantById_NotFound() {
        assertThrows(ResourceNotFoundException.class, () -> {
            merchantRepository.findById(999L).orElseThrow(() -> new ResourceNotFoundException("Merchant not found"));
        });
    }

    @Test
    public void testGetAllActiveMerchants() {
        Merchant merchant = new Merchant();
        merchant.setName("Active Merchant");
        merchant.setDescription("Active merchant description");
        merchant.setCategory("Retail");
        merchant.setLogoUrl("http://example.com/logo.png");
        merchant.setWebsite("http://example.com");
        merchant.setActive(true);
        merchantRepository.save(merchant);

        List<MerchantDTO> activeMerchants = merchantRepository.findByActiveTrue().stream()
                .map(merchantMapper::toDTO)
                .collect(Collectors.toList());

        assertFalse(activeMerchants.isEmpty());
        assertEquals(1, activeMerchants.size());
        assertEquals("Active Merchant", activeMerchants.get(0).getName());
    }
}