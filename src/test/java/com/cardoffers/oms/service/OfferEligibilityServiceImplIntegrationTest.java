package com.cardoffers.oms.service;

import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import com.cardoffers.oms.exception.CardholderNotEligibleException;
import com.cardoffers.oms.exception.ResourceNotFoundException;
import com.cardoffers.oms.mapper.OfferMapper;
import com.cardoffers.oms.model.dto.EligibleOfferResponseDTO;
import com.cardoffers.oms.model.dto.OfferDTO;
import com.cardoffers.oms.model.entity.Cardholder;
import com.cardoffers.oms.model.entity.CardholderCard;
import com.cardoffers.oms.model.entity.Offer;
import com.cardoffers.oms.repository.CardholderCardRepository;
import com.cardoffers.oms.repository.CardholderRepository;
import com.cardoffers.oms.repository.OfferRepository;
import com.cardoffers.oms.service.OfferEligibilityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(exclude = {RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class, CacheAutoConfiguration.class})
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.cache.type=none"})
class OfferEligibilityServiceImplIntegrationTest {

    @Autowired
    private CardholderRepository cardholderRepository;

    @Autowired
    private CardholderCardRepository cardholderCardRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private OfferMapper offerMapper;

    @Autowired
    private OfferEligibilityService offerEligibilityServiceProxy;

    @Autowired
    private OfferEligibilityService offerEligibilityService;

    // S-27c: SEEDING — deterministic @BeforeEach injected by V3 postprocessor
    // Root cause: LLM-generated @BeforeEach lacked .save() → DB empty → 404/empty list
    @BeforeEach
    void setUp() {
        Cardholder cardholder = new Cardholder();
        cardholder.setFirstName("Test firstName");
        cardholder.setLastName("Test lastName");
        cardholder.setEmail("test@example.com");
        cardholder.setCreatedAt(java.time.LocalDateTime.now());
        cardholder.setUpdatedAt(java.time.LocalDateTime.now());
        cardholderRepository.save(cardholder);
    }

    @Test
void shouldFilterOffersByCategory_happyPath() {
    // Given
    Long cardholderId = 1L;
    String category = "Electronics";

    Cardholder cardholder = new Cardholder();
    cardholder.setEmail("test@example.com");
    
    Offer offer1 = new Offer();
    offer1.setTitle("Discount on TVs");
    offer1.setCategory(category);
    offer1.setActive(true);
    offer1.setCreatedAt(LocalDateTime.now());

    Offer offer2 = new Offer();
    offer2.setTitle("Discount on Laptops");
    offer2.setCategory(category);
    offer2.setActive(true);
    offer2.setCreatedAt(LocalDateTime.now());

    List<Offer> offers = Arrays.asList(offer1, offer2);

    // When
    List<OfferDTO> result = offerEligibilityService.filterOffersByCategory(cardholderId, category);

    // Then
    assertTrue(result.isEmpty());
    assertEquals(2, result.size());
    assertEquals("Discount on TVs", result.get(0).getTitle());
    assertEquals("Discount on Laptops", result.get(1).getTitle());
}

    @Test
void shouldFilterOffersByOfferType_happyPath() {
    // Given
    Long cardholderId = 1L;
    String offerType = "Discount";
    Offer offer = new Offer();
    offer.setId(1L);
    offer.setTitle("Summer Sale");
    offer.setDescription("Get 20% off");
    offer.setOfferType(offerType);
    offer.setActive(true);
    offer.setDiscountPercentage(BigDecimal.valueOf(20));
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(30));
    offer.setCreatedAt(LocalDateTime.now());
    offer.setUpdatedAt(java.time.LocalDateTime.now());
    offerRepository.save(offer);


    // When
    List<OfferDTO> result = offerEligibilityService.filterOffersByOfferType(cardholderId, offerType);

    // Then
    assertTrue(result.isEmpty());
    assertEquals(1, result.size());
    assertEquals(offerType, result.get(0).getOfferType());
}

    @Test
void shouldGetEligibleOffersForCardholder_nullId() {
    // Given
    Long cardholderId = null;

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> {
        offerEligibilityService.getEligibleOffersForCardholder(cardholderId);
    });
}

    @Test
void shouldGetEligibleOffersForCardholder_nonExistentId() {
    // Given
    Long cardholderId = 999L; // Assuming this ID does not exist in the database

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> {
        offerEligibilityService.getEligibleOffersForCardholder(cardholderId);
    });
}

    @Test
void shouldGetEligibleOffersForCardholder_noActiveCards() {
    // Given
    Long cardholderId = 1L; // example cardholderId

    // When & Then
    assertThrows(CardholderNotEligibleException.class, () -> 
        offerEligibilityService.getEligibleOffersForCardholder(cardholderId)
    );
}

    @Test
void shouldGetEligibleOffersForCardholder_multipleActiveCards() {
    // Given
    Long cardholderId = 1L;
    Cardholder cardholder = new Cardholder();
    cardholder.setId(cardholderId);
    cardholder.setEmail("test@example.com");
    cardholder.setActive(true);
    
    CardholderCard activeCard1 = new CardholderCard();
    activeCard1.setCardholderId(cardholderId);
    activeCard1.setActive(true);
    
    CardholderCard activeCard2 = new CardholderCard();
    activeCard2.setCardholderId(cardholderId);
    activeCard2.setActive(true);
    
    Offer offer1 = new Offer();
    offer1.setId(1L);
    offer1.setTitle("Offer 1");
    offer1.setCardNetwork(CardNetwork.CARD_NETWORK_A);
    
    Offer offer2 = new Offer();
    offer2.setId(2L);
    offer2.setTitle("Offer 2");
    offer2.setCardNetwork(CardNetwork.CARD_NETWORK_B);
    

    // When
    EligibleOfferResponseDTO result = offerEligibilityService.getEligibleOffersForCardholder(cardholderId);

    // Then
    assertNotNull(result);
    assertEquals(cardholderId, result.getCardholderId());
    assertEquals(2, result.getEligibleOffers().size());
    assertEquals("Offer 1", result.getEligibleOffers().get(0).getTitle());
    assertEquals("Offer 2", result.getEligibleOffers().get(1).getTitle());
}

}