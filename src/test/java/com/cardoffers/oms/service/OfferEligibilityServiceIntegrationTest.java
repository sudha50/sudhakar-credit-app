package com.cardoffers.oms.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import com.cardoffers.oms.model.dto.EligibleOfferResponseDTO;
import com.cardoffers.oms.model.dto.OfferDTO;
import com.cardoffers.oms.service.OfferEligibilityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.*;
import com.cardoffers.oms.model.dto.CardNetworkDTO;
import com.cardoffers.oms.model.dto.MerchantDTO;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.cache.type=none",
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration,org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration"
    })
class OfferEligibilityServiceIntegrationTest {

    @Autowired
    private OfferEligibilityService offerEligibilityService;

    @Test
void shouldFilterOffersByCategory_happyPath() {
    // Given
    Long cardholderId = 1L;
    String category = "Electronics";

    MerchantDTO merchant = new MerchantDTO();
    merchant.setName("Tech Store");
    // Set other required fields for MerchantDTO if necessary

    CardNetworkDTO cardNetwork = new CardNetworkDTO();
    cardNetwork.setName("Visa");
    // Set other required fields for CardNetworkDTO if necessary

    OfferDTO offer = new OfferDTO();
    offer.setTitle("Discount on Gadgets");
    offer.setOfferType("Electronics");
    offer.setDiscountPercentage(new BigDecimal("10"));
    offer.setCashbackAmount(new BigDecimal("5"));
    offer.setMinimumPurchaseAmount(new BigDecimal("50"));
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(30));
    offer.setSource("Online");
    offer.setMerchant(merchant);
    offer.setCardNetwork(cardNetwork);
    // Set other required fields for OfferDTO if necessary

    offerRepository.save(offer); // Assuming there's an offerRepository to save offers

    // When
    List<OfferDTO> result = offerEligibilityService.filterOffersByCategory(cardholderId, category);

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
    assertEquals(1, result.size());
    assertEquals("Discount on Gadgets", result.get(0).getTitle());
    assertEquals("Electronics", result.get(0).getOfferType());
}

    @Test
void shouldFilterOffersByOfferType_happyPath() {
    // Given
    Long cardholderId = 1L;
    String offerType = "Discount";
    
    MerchantDTO merchant = new MerchantDTO();
    merchant.setId(1L);
    // Set other required fields...

    CardNetworkDTO cardNetwork = new CardNetworkDTO();
    cardNetwork.setId(1L);
    // Set other required fields...

    OfferDTO offer = new OfferDTO();
    offer.setId(1L);
    offer.setTitle("Special Discount");
    offer.setOfferType(offerType);
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(10));
    offer.setMerchant(merchant);
    offer.setCardNetwork(cardNetwork);
    // Set other required fields...

    // Save offer in the repository (assuming a repository exists)
    offerRepository.save(offer);

    // When
    List<OfferDTO> result = offerEligibilityService.filterOffersByOfferType(cardholderId, offerType);

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(offerType, result.get(0).getOfferType());
    assertEquals("Special Discount", result.get(0).getTitle());
}

    @Test
void shouldGetEligibleOffersForCardholder_nullCardholderId() {
    // Given
    Long cardholderId = null;

    // When and Then
    assertThrows(IllegalArgumentException.class, () -> offerEligibilityService.getEligibleOffersForCardholder(cardholderId));
}

    @Test
void shouldGetEligibleOffersForCardholder_emptyResponse() {
    // Given
    Long cardholderId = 1L; // cardholder with no active cards

    // When
    EligibleOfferResponseDTO result = offerEligibilityService.getEligibleOffersForCardholder(cardholderId);

    // Then
    assertNotNull(result);
    assertEquals(cardholderId, result.getCardholderId());
    assertNull(result.getCardholderName());
    assertNotNull(result.getEligibleOffers());
    assertTrue(result.getEligibleOffers().isEmpty());
    assertEquals(0, result.getTotalOffers().intValue());
}

    @Test
void shouldFilterOffersByCategory_emptyCategory() {
    // Given
    Long cardholderId = 1L;
    String category = "";
    
    // When
    List<OfferDTO> result = offerEligibilityService.filterOffersByCategory(cardholderId, category);
    
    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
}

    @Test
void shouldFilterOffersByOfferType_nullOfferType() {
    // Given
    Long cardholderId = 1L;

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> {
        offerEligibilityService.filterOffersByOfferType(cardholderId, null);
    });
}

}