package com.cardoffers.oms.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.containers.PostgreSQLContainer;
import org.springframework.boot.testcontainers.junit.jupiter.Container;
import org.springframework.boot.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import com.cardoffers.oms.exception.ResourceNotFoundException;
import com.cardoffers.oms.model.dto.MerchantDTO;
import com.cardoffers.oms.repository.MerchantRepository;
import com.cardoffers.oms.mapper.MerchantMapper;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class MerchantServiceImplIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureDatabaseProperties(DynamicPropertySourceRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private MerchantMapper merchantMapper;

    @Test
    public void createMerchant_shouldReturnCreatedMerchant() {
        MerchantDTO merchantDTO = new MerchantDTO();
        merchantDTO.setName("Test Merchant");
        merchantDTO.setDescription("A merchant for testing");
        merchantDTO.setCategory("Test Category");
        merchantDTO.setLogoUrl("http://example.com/logo.png");
        merchantDTO.setWebsite("http://example.com");
        merchantDTO.setActive(true);

        MerchantDTO createdMerchant = merchantService.createMerchant(merchantDTO);
        assertNotNull(createdMerchant);
        assertEquals("Test Merchant", createdMerchant.getName());
    }

    @Test
    public void getMerchantById_shouldReturnMerchantWhenExists() {
        MerchantDTO merchantDTO = new MerchantDTO();
        merchantDTO.setName("Test Merchant");
        merchantDTO.setDescription("A merchant for testing");
        merchantDTO.setCategory("Test Category");
        merchantDTO.setLogoUrl("http://example.com/logo.png");
        merchantDTO.setWebsite("http://example.com");
        merchantDTO.setActive(true);

        MerchantDTO createdMerchant = merchantService.createMerchant(merchantDTO);
        MerchantDTO foundMerchant = merchantService.getMerchantById(createdMerchant.getId());

        assertNotNull(foundMerchant);
        assertEquals(createdMerchant.getId(), foundMerchant.getId());
        assertEquals(createdMerchant.getName(), foundMerchant.getName());
    }

    @Test
    public void getMerchantById_shouldThrowExceptionWhenMerchantNotFound() {
        assertThrows(ResourceNotFoundException.class, () -> {
            merchantService.getMerchantById(999L);
        });
    }

    @Test
    public void getMerchantsByCategory_shouldReturnActiveMerchantsInCategory() {
        MerchantDTO merchantDTO1 = new MerchantDTO();
        merchantDTO1.setName("Active Merchant 1");
        merchantDTO1.setDescription("An active merchant");
        merchantDTO1.setCategory("Food");
        merchantDTO1.setLogoUrl("http://example.com/logo1.png");
        merchantDTO1.setWebsite("http://example.com/1");
        merchantDTO1.setActive(true);
        
        MerchantDTO merchantDTO2 = new MerchantDTO();
        merchantDTO2.setName("Inactive Merchant");
        merchantDTO2.setDescription("An inactive merchant");
        merchantDTO2.setCategory("Food");
        merchantDTO2.setLogoUrl("http://example.com/logo2.png");
        merchantDTO2.setWebsite("http://example.com/2");
        merchantDTO2.setActive(false);

        merchantService.createMerchant(merchantDTO1);
        merchantService.createMerchant(merchantDTO2);

        List<MerchantDTO> activeMerchants = merchantService.getMerchantsByCategory("Food");
        assertEquals(1, activeMerchants.size());
        assertEquals("Active Merchant 1", activeMerchants.get(0).getName());
    }
}