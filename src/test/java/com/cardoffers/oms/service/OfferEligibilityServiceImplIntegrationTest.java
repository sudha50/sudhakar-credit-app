package com.cardoffers.oms.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.AutoConfigureContainer;
import org.springframework.boot.testcontainers.Testcontainers;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import com.cardoffers.oms.exception.CardholderNotEligibleException;
import com.cardoffers.oms.exception.ResourceNotFoundException;
import com.cardoffers.oms.model.dto.EligibleOfferResponseDTO;
import com.cardoffers.oms.model.dto.OfferDTO;
import com.cardoffers.oms.model.entity.Cardholder;
import com.cardoffers.oms.model.entity.CardholderCard;
import com.cardoffers.oms.model.entity.Offer;
import com.cardoffers.oms.repository.CardholderCardRepository;
import com.cardoffers.oms.repository.CardholderRepository;
import com.cardoffers.oms.repository.OfferRepository;

import org.springframework.boot.test.web.client.TestRestTemplate;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestConfiguration.class) // Assuming a configuration @ActiveProfiles("test")
class sets up the necessary beans.
class OfferEligibilityServiceImplIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgresContainer = 
        new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
            .withDatabaseName("test_db")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Autowired
    private OfferEligibilityServiceImpl offerEligibilityService;

    @Autowired
    private CardholderRepository cardholderRepository;

    @Autowired
    private CardholderCardRepository cardholderCardRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Test
    void getEligibleOffersForCardholder_ReturnsEligibleOffers() {
        Cardholder cardholder = new Cardholder();
        cardholder.setFirstName("John");
        cardholder.setLastName("Doe");
        cardholder.setId(1L);
        cardholderRepository.save(cardholder);
        
        CardholderCard card = new CardholderCard();
        card.setCardholderId(1L);
        card.setActive(true);
        cardholderCardRepository.save(card);

        Offer offer = new Offer();
        offer.setCardNetwork(card);
        offer.setMaxRedemptions(5);
        offer.setCurrentRedemptions(0);
        offer.setActive(true);
        offer.setStartDate(LocalDate.now());
        offer.setEndDate(LocalDate.now().plusDays(10));
        offerRepository.save(offer);

        EligibleOfferResponseDTO responseDTO = offerEligibilityService.getEligibleOffersForCardholder(1L);
        assertNotNull(responseDTO);
        assertEquals(1, responseDTO.getTotalOffers());
        assertEquals("John Doe", responseDTO.getCardholderName());
    }

    @Test
    void getEligibleOffersForCardholder_NoActiveCards_ThrowsException() {
        Cardholder cardholder = new Cardholder();
        cardholder.setFirstName("Jane");
        cardholder.setLastName("Smith");
        cardholder.setId(2L);
        cardholderRepository.save(cardholder);

        Exception exception = assertThrows(CardholderNotEligibleException.class, 
            () -> offerEligibilityService.getEligibleOffersForCardholder(2L));
        assertEquals("Cardholder has no active cards", exception.getMessage());
    }

    @Test
    void getEligibleOffersForCardholder_CardholderNotFound_ThrowsException() {
        Exception exception = assertThrows(ResourceNotFoundException.class, 
            () -> offerEligibilityService.getEligibleOffersForCardholder(999L));
        assertEquals("Cardholder not found", exception.getMessage());
    }

    @Test
    void filterOffersByCategory_ReturnsFilteredOffers() {
        Cardholder cardholder = new Cardholder();
        cardholder.setFirstName("Alice");
        cardholder.setLastName("Johnson");
        cardholder.setId(3L);
        cardholderRepository.save(cardholder);

        CardholderCard card = new CardholderCard();
        card.setCardholderId(3L);
        card.setActive(true);
        cardholderCardRepository.save(card);

        Offer offer1 = new Offer();
        offer1.setCardNetwork(card);
        offer1.setMaxRedemptions(5);
        offer1.setCurrentRedemptions(0);
        offer1.setActive(true);
        offer1.setMerchant(new Merchant("Electronics"));
        offerRepository.save(offer1);

        Offer offer2 = new Offer();
        offer2.setCardNetwork(card);
        offer2.setMaxRedemptions(5);
        offer2.setCurrentRedemptions(1);
        offer2.setActive(true);
        offer2.setMerchant(new Merchant("Groceries"));
        offerRepository.save(offer2);

        List<OfferDTO> filteredOffers = offerEligibilityService.filterOffersByCategory(3L, "Electronics");
        assertNotNull(filteredOffers);
        assertEquals(1, filteredOffers.size());
        assertEquals("Electronics", filteredOffers.get(0).getMerchant().getCategory());
    }
}