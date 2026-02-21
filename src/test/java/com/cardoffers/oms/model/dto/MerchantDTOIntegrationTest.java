package com.cardoffers.oms.model.dto;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.kafka.TestcontainersKafka;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.assertj.core.api.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestcontainersKafka
@ActiveProfiles("test")
class MerchantDTOIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
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
    private MerchantRepository merchantRepository; // Assume there's a MerchantRepository

    @Test
    void whenSaveValidMerchant_thenMerchantIsPersisted() {
        MerchantDTO merchant = new MerchantDTO();
        merchant.setName("Test Merchant");
        merchant.setDescription("A merchant for testing");
        merchant.setCategory("Category1");
        merchant.setLogoUrl("http://example.com/logo.png");
        merchant.setWebsite("http://example.com");
        merchant.setActive(true);

        merchantRepository.save(merchant); // Assume a save method exists

        assertThat(merchantRepository.findById(merchant.getId())).isPresent();
    }

    @Test
    void whenSaveMerchantWithoutName_thenThrowsConstraintViolationException() {
        MerchantDTO merchant = new MerchantDTO();
        merchant.setDescription("A merchant without name");
        merchant.setCategory("Category1");
        merchant.setLogoUrl("http://example.com/logo.png");
        merchant.setWebsite("http://example.com");
        merchant.setActive(true);

        assertThrows(ConstraintViolationException.class, () -> {
            merchantRepository.save(merchant);
        });
    }

    @Test
    void whenRetrieveMerchant_thenReturnsCorrectMerchant() {
        MerchantDTO merchant = new MerchantDTO();
        merchant.setName("Sample Merchant");
        merchant.setDescription("A sample merchant for testing");
        merchant.setCategory("SampleCategory");
        merchant.setLogoUrl("http://example.com/sample-logo.png");
        merchant.setWebsite("http://example.com/sample");
        merchant.setActive(true);

        merchant = merchantRepository.save(merchant);

        MerchantDTO retrievedMerchant = merchantRepository.findById(merchant.getId()).orElse(null);

        assertThat(retrievedMerchant).isNotNull();
        assertThat(retrievedMerchant.getName()).isEqualTo("Sample Merchant");
    }
}