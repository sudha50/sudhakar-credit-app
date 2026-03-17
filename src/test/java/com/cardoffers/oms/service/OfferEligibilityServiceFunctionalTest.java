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
import org.springframework.boot.test.mock.mockito.MockBean;
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
class OfferEligibilityServiceFunctionalTest {

    @Autowired
    private OfferEligibilityService offerEligibilityService;

    @Test
void shouldFilterOffersByCategory_happyPath() {
    // Given
    Long cardholderId = 1L;
    String category = "Travel";

    OfferDTO offer1 = new OfferDTO();
    offer1.setId(1L);
    offer1.setTitle("Travel Deal");
    offer1.setOfferType("Discount");
    offer1.setDiscountPercentage(BigDecimal.valueOf(10));
    offer1.setMinimumPurchaseAmount(BigDecimal.valueOf(100));
    offer1.setStartDate(LocalDate.now());
    offer1.setEndDate(LocalDate.now().plusDays(30));
    offer1.setSource("Website");
    offer1.setActive(true);
    
    MerchantDTO merchant = new MerchantDTO();
    // Set required MerchantDTO fields 
    // Assuming these required fields for MerchantDTO
    merchant.setName("Merchant A");
    offer1.setMerchant(merchant);
    
    CardNetworkDTO cardNetwork = new CardNetworkDTO();
    // Set required CardNetworkDTO fields 
    // Assuming these required fields for CardNetworkDTO
    cardNetwork.setName("Visa");
    offer1.setCardNetwork(cardNetwork);
    
    List<OfferDTO> expectedOffers = Collections.singletonList(offer1);
    

    // When
    List<OfferDTO> result = offerEligibilityService.filterOffersByCategory(cardholderId, category);

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Travel Deal", result.get(0).getTitle());
    assertEquals("Discount", result.get(0).getOfferType());
    assertEquals(BigDecimal.valueOf(10), result.get(0).getDiscountPercentage());
    assertEquals("Website", result.get(0).getSource());
}

    @Test
void shouldFilterOffersByOfferType_happyPath() {
    // Given
    Long cardholderId = 1L;
    String offerType = "Cashback";

    OfferDTO offer1 = new OfferDTO();
    offer1.setId(1L);
    offer1.setTitle("Cashback Offer");
    offer1.setOfferType(offerType);
    offer1.setStartDate(LocalDate.now());
    offer1.setEndDate(LocalDate.now().plusDays(30));
    offer1.setSource("Website");
    
    OfferDTO offer2 = new OfferDTO();
    offer2.setId(2L);
    offer2.setTitle("Another Offer");
    offer2.setOfferType("Discount");
    offer2.setStartDate(LocalDate.now());
    offer2.setEndDate(LocalDate.now().plusDays(30));
    offer2.setSource("Mobile App");
    
    // Assuming there's a method to save offers, which can be done if there's a repository setup
    offerRepository.save(offer1);
    offerRepository.save(offer2); // Ensure that offers are saved for testing.

    // When
    List<OfferDTO> result = offerEligibilityService.filterOffersByOfferType(cardholderId, offerType);

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(offerType, result.get(0).getOfferType());
    assertEquals(offer1.getId(), result.get(0).getId());
    assertEquals(offer1.getTitle(), result.get(0).getTitle());
}

    @Test
void shouldGetEligibleOffersForCardholder_nullCardholderId() {
    // Given
    Long cardholderId = null;

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> offerEligibilityService.getEligibleOffersForCardholder(cardholderId));
}

    @Test
void shouldFilterOffersByCategory_emptyCategory() {
    // Given
    Long cardholderId = 123L;
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

    @Test
void shouldGetEligibleOffersForCardholder_nonExistentCardholder() {
    // Given
    Long nonExistentCardholderId = 999L;

    // When
    EligibleOfferResponseDTO result = offerEligibilityService.getEligibleOffersForCardholder(nonExistentCardholderId);

    // Then
    assertNotNull(result);
    assertEquals(nonExistentCardholderId, result.getCardholderId());
    assertNull(result.getCardholderName());
    assertNotNull(result.getEligibleOffers());
    assertTrue(result.getEligibleOffers().isEmpty());
    assertEquals(0, result.getTotalOffers());
}

}