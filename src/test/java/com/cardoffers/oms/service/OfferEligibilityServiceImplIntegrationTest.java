package com.cardoffers.oms.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.cardoffers.oms.exception.CardholderNotEligibleException;
import com.cardoffers.oms.exception.ResourceNotFoundException;
import com.cardoffers.oms.model.dto.EligibleOfferResponseDTO;
import com.cardoffers.oms.repository.CardholderCardRepository;
import com.cardoffers.oms.repository.CardholderRepository;
import com.cardoffers.oms.repository.OfferRepository;
import com.cardoffers.oms.mapper.OfferMapper;

@Testcontainers
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import({OfferEligibilityServiceImpl.class})
@ActiveProfiles("test")
class OfferEligibilityServiceImplIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> databaseContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private OfferEligibilityServiceImpl offerEligibilityService;

    @Autowired
    private CardholderRepository cardholderRepository;

    @Autowired
    private CardholderCardRepository cardholderCardRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private OfferMapper offerMapper;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", databaseContainer::getJdbcUrl);
        registry.add("spring.datasource.username", databaseContainer::getUsername);
        registry.add("spring.datasource.password", databaseContainer::getPassword);
    }

    @Test
    void testGetEligibleOffersForCardholder_HappyPath() {
        // Setup test data
        Cardholder cardholder = new Cardholder();
        cardholder.setFirstName("John");
        cardholder.setLastName("Doe");
        cardholder.setId(1L);
        cardholderRepository.save(cardholder);

        CardholderCard card = new CardholderCard();
        card.setCardholder(cardholder);
        card.setActive(true);
        card.setId(1L);
        cardholderCardRepository.save(card);

        Offer offer = new Offer();
        offer.setId(1L);
        offer.setCardNetwork(card.getCardNetwork());
        offer.setMaxRedemptions(10);
        offer.setCurrentRedemptions(5);
        offer.setActive(true);
        offerRepository.save(offer);

        // Test the service
        EligibleOfferResponseDTO response = offerEligibilityService.getEligibleOffersForCardholder(cardholder.getId());

        assertNotNull(response);
        assertEquals(cardholder.getId(), response.getCardholderId());
        assertFalse(response.getEligibleOffers().isEmpty());
        assertEquals(1, response.getTotalOffers());
    }

    @Test
    void testGetEligibleOffersForCardholder_NoActiveCards() {
        Cardholder cardholder = new Cardholder();
        cardholder.setFirstName("Jane");
        cardholder.setLastName("Smith");
        cardholder.setId(2L);
        cardholderRepository.save(cardholder);

        assertThrows(CardholderNotEligibleException.class, () -> {
            offerEligibilityService.getEligibleOffersForCardholder(cardholder.getId());
        });
    }

    @Test
    void testGetEligibleOffersForCardholder_CardholderNotFound() {
        assertThrows(ResourceNotFoundException.class, () -> {
            offerEligibilityService.getEligibleOffersForCardholder(999L);
        });
    }

    @Test
    void testFilterOffersByCategory() {
        // Setup test data similar to the first test
        Cardholder cardholder = new Cardholder();
        cardholder.setFirstName("Alice");
        cardholder.setLastName("Brown");
        cardholder.setId(3L);
        cardholderRepository.save(cardholder);

        CardholderCard card = new CardholderCard();
        card.setCardholder(cardholder);
        card.setActive(true);
        card.setId(2L);
        cardholderCardRepository.save(card);

        Offer offer = new Offer();
        offer.setId(2L);
        offer.setCardNetwork(card.getCardNetwork());
        offer.setMaxRedemptions(5);
        offer.setCurrentRedemptions(0);
        offer.setActive(true);
        offer.setMerchant(merchantWithCategory("Electronics")); // Assuming a method to set a merchant with category
        offerRepository.save(offer);

        List<OfferDTO> filteredOffers = offerEligibilityService.filterOffersByCategory(cardholder.getId(), "Electronics");
        
        assertFalse(filteredOffers.isEmpty());
        assertEquals(1, filteredOffers.size());
        assertEquals("Electronics", filteredOffers.get(0).getMerchant().getCategory());
    }
}