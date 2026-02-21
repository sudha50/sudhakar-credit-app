package com.cardoffers.oms.model.dto;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootTest
@ActiveProfiles("test")
class OfferSummaryDTOIntegrationTest {

    @Autowired
    private OfferSummaryDTORepository offerSummaryDTORepository;

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("password");

    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Test
    void testSaveOfferSummary() {
        OfferSummaryDTO offerSummaryDTO = new OfferSummaryDTO();
        offerSummaryDTO.setTitle("Summer Sale");
        offerSummaryDTO.setMerchantName("Best Merchant");
        offerSummaryDTO.setOfferType("Discount");
        offerSummaryDTO.setDiscountPercentage(new BigDecimal("20.5"));
        offerSummaryDTO.setStartDate(LocalDate.now());
        offerSummaryDTO.setEndDate(LocalDate.now().plusDays(30));
        offerSummaryDTO.setActive(true);

        OfferSummaryDTO savedOffer = offerSummaryDTORepository.save(offerSummaryDTO);

        assertNotNull(savedOffer.getId());
        assertEquals("Summer Sale", savedOffer.getTitle());
    }

    @Test
    void testInvalidOfferSummarySave() {
        OfferSummaryDTO offerSummaryDTO = new OfferSummaryDTO();
        offerSummaryDTO.setMerchantName(null); // Title is missing

        Exception exception = assertThrows(Exception.class, () -> {
            offerSummaryDTORepository.save(offerSummaryDTO);
        });

        String expectedMessage = "could not execute statement; SQL [n/a]; constraint [null];";
        String actualMessage = exception.getCause().getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}