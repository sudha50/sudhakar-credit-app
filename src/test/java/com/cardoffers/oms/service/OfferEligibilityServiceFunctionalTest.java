package com.cardoffers.oms.service;

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


@SpringBootTest(exclude = {RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class, CacheAutoConfiguration.class})
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.cache.type=none"})
class OfferEligibilityServiceFunctionalTest {

    @Autowired
    private OfferEligibilityService offerEligibilityService;

    @Test
void shouldFilterOffersByCategory_happyPath() {
    // Given
    Long cardholderId = 1L;
    String category = "Electronics";
    
    MerchantDTO merchant = new MerchantDTO();
    // set required fields for MerchantDTO
    CardNetworkDTO cardNetwork = new CardNetworkDTO();
    // set required fields for CardNetworkDTO

    OfferDTO offer = new OfferDTO();
    offer.setId(1L);
    offer.setTitle("Discount on Electronics");
    offer.setOfferType("Sale");
    offer.setDiscountPercentage(new BigDecimal("10.00"));
    offer.setCashbackAmount(new BigDecimal("5.00"));
    offer.setMinimumPurchaseAmount(new BigDecimal("50.00"));
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(10));
    offer.setTermsAndConditions("Terms apply");
    offer.setMerchant(merchant);
    offer.setCardNetwork(cardNetwork);
    offer.setSource("App");
    offer.setMaxRedemptions(100);
    offer.setCurrentRedemptions(0);
    offer.setActive(true);


    // When
    List<OfferDTO> result = offerEligibilityService.filterOffersByCategory(cardholderId, category);

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    OfferDTO returnedOffer = result.get(0);
    assertEquals(1L, returnedOffer.getId());
    assertEquals("Discount on Electronics", returnedOffer.getTitle());
    assertEquals("Sale", returnedOffer.getOfferType());
    assertEquals(new BigDecimal("10.00"), returnedOffer.getDiscountPercentage());
    assertEquals(new BigDecimal("5.00"), returnedOffer.getCashbackAmount());
    assertEquals(new BigDecimal("50.00"), returnedOffer.getMinimumPurchaseAmount());
    returnedOffer.prePersist();
    assertNotNull(returnedOffer.getStartDate());
    returnedOffer.prePersist();
    assertNotNull(returnedOffer.getEndDate());
    assertEquals("Terms apply", returnedOffer.getTermsAndConditions());
    assertNotNull(returnedOffer.getMerchant());
    assertNotNull(returnedOffer.getCardNetwork());
    assertEquals("App", returnedOffer.getSource());
    assertEquals(Integer.valueOf(100), returnedOffer.getMaxRedemptions());
    assertEquals(Integer.valueOf(0), returnedOffer.getCurrentRedemptions());
    assertNotNull(returnedOffer.getActive());
    assertTrue(returnedOffer.getActive());
}

    @Test
void shouldFilterOffersByOfferType_happyPath() {
    // Given
    Long cardholderId = 1L;
    String offerType = "Discount";
    
    MerchantDTO merchant = new MerchantDTO();
    // Set required fields for MerchantDTO here

    CardNetworkDTO cardNetwork = new CardNetworkDTO();
    // Set required fields for CardNetworkDTO here

    OfferDTO offer = new OfferDTO();
    offer.setId(1L);
    offer.setTitle("Special Discount");
    offer.setDescription("Get 20% off");
    offer.setOfferType(offerType);
    offer.setDiscountPercentage(new BigDecimal("20.00"));
    offer.setMinimumPurchaseAmount(new BigDecimal("100.00"));
    offer.setCashbackAmount(new BigDecimal("10.00"));
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(30));
    offer.setTermsAndConditions("Terms apply");
    offer.setMerchant(merchant);
    offer.setCardNetwork(cardNetwork);
    offer.setSource("App");
    offer.setMaxRedemptions(100);
    offer.setCurrentRedemptions(0);
    offer.setActive(true);


    // When
    List<OfferDTO> result = offerEligibilityService.filterOffersByOfferType(cardholderId, offerType);

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    OfferDTO returnedOffer = result.get(0);
    assertEquals("Special Discount", returnedOffer.getTitle());
    assertEquals("Get 20% off", returnedOffer.getDescription());
    assertEquals(offerType, returnedOffer.getOfferType());
    assertEquals(new BigDecimal("20.00"), returnedOffer.getDiscountPercentage());
    assertEquals(new BigDecimal("100.00"), returnedOffer.getMinimumPurchaseAmount());
    assertEquals(new BigDecimal("10.00"), returnedOffer.getCashbackAmount());
    assertEquals("Terms apply", returnedOffer.getTermsAndConditions());
    assertEquals("App", returnedOffer.getSource());
}

    @Test
void shouldGetEligibleOffersForCardholder_nullId() {
    // Given
    Long cardholderId = null;

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> offerEligibilityService.getEligibleOffersForCardholder(cardholderId));
}

    @Test
void shouldGetEligibleOffersForCardholder_emptyId() {
    // Given
    Long cardholderId = 0L;

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> {
        offerEligibilityService.getEligibleOffersForCardholder(cardholderId);
    });
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
    String offerType = null;

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> {
        offerEligibilityService.filterOffersByOfferType(cardholderId, offerType);
    });
}

}