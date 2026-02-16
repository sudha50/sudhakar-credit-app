package com.cardoffers.oms.service;

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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.cardoffers.oms.exception.ResourceNotFoundException;
import com.cardoffers.oms.mapper.MerchantMapper;
import com.cardoffers.oms.model.dto.MerchantDTO;
import com.cardoffers.oms.model.entity.Merchant;
import com.cardoffers.oms.repository.MerchantRepository;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
public class MerchantServiceImplIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("password");

    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @Autowired
    private MerchantServiceImpl merchantService;

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private MerchantMapper merchantMapper;

    @Test
    public void testCreateMerchant() {
        MerchantDTO merchantDTO = new MerchantDTO();
        merchantDTO.setName("Test Merchant");
        merchantDTO.setDescription("A test merchant");
        merchantDTO.setCategory("Test Category");
        merchantDTO.setLogoUrl("http://logo.url/test.png");
        merchantDTO.setWebsite("http://www.testmerchant.com");
        merchantDTO.setActive(true);

        MerchantDTO createdMerchant = merchantService.createMerchant(merchantDTO);

        assertNotNull(createdMerchant);
        assertEquals(merchantDTO.getName(), createdMerchant.getName());
        assertEquals(merchantDTO.getDescription(), createdMerchant.getDescription());
    }

    @Test
    public void testGetMerchantById() {
        Merchant merchant = new Merchant();
        merchant.setName("Existing Merchant");
        merchant.setDescription("An existing merchant");
        merchant.setCategory("Existing Category");
        merchant.setLogoUrl("http://logo.url/existing.png");
        merchant.setWebsite("http://www.existingmerchant.com");
        merchant.setActive(true);
        merchant = merchantRepository.save(merchant);

        MerchantDTO foundMerchant = merchantService.getMerchantById(merchant.getId());

        assertNotNull(foundMerchant);
        assertEquals(merchant.getName(), foundMerchant.getName());
    }

    @Test
    public void testGetMerchantsByCategory() {
        Merchant merchant1 = new Merchant();
        merchant1.setName("Merchant A");
        merchant1.setDescription("Description A");
        merchant1.setCategory("Category A");
        merchant1.setLogoUrl("http://logo.url/1.png");
        merchant1.setWebsite("http://www.merchant1.com");
        merchant1.setActive(true);
        merchantRepository.save(merchant1);

        Merchant merchant2 = new Merchant();
        merchant2.setName("Merchant B");
        merchant2.setDescription("Description B");
        merchant2.setCategory("Category A");
        merchant2.setLogoUrl("http://logo.url/2.png");
        merchant2.setWebsite("http://www.merchant2.com");
        merchant2.setActive(false);
        merchantRepository.save(merchant2);

        List<MerchantDTO> merchants = merchantService.getMerchantsByCategory("Category A");
        
        assertEquals(1, merchants.size());
        assertEquals("Merchant A", merchants.get(0).getName());
    }

    @Test
    public void testUpdateMerchant() {
        Merchant merchant = new Merchant();
        merchant.setName("Old Merchant");
        merchant.setDescription("Old Description");
        merchant.setCategory("Old Category");
        merchant.setLogoUrl("http://logo.url/old.png");
        merchant.setWebsite("http://www.oldmerchant.com");
        merchant.setActive(true);
        merchant = merchantRepository.save(merchant);

        MerchantDTO updateDTO = new MerchantDTO();
        updateDTO.setName("Updated Merchant");
        updateDTO.setDescription("Updated Description");
        updateDTO.setCategory("Updated Category");
        updateDTO.setLogoUrl("http://logo.url/updated.png");
        updateDTO.setWebsite("http://www.updatedmerchant.com");
        updateDTO.setActive(false);

        MerchantDTO updatedMerchant = merchantService.updateMerchant(merchant.getId(), updateDTO);

        assertNotNull(updatedMerchant);
        assertEquals("Updated Merchant", updatedMerchant.getName());
        assertEquals("Updated Description", updatedMerchant.getDescription());
    }
}