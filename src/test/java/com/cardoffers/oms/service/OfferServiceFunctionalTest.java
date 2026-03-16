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
import com.cardoffers.oms.model.dto.OfferDTO;
import com.cardoffers.oms.model.dto.OfferSummaryDTO;
import com.cardoffers.oms.service.OfferService;
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
class OfferServiceFunctionalTest {

    @Autowired
    private OfferService offerService;

    @Test
void shouldCreateOffer_happyPath() {
    // Given
    MerchantDTO merchant = new MerchantDTO();
    merchant.setId(1L);
    // Assume merchant has necessary fields set as required.

    CardNetworkDTO cardNetwork = new CardNetworkDTO();
    cardNetwork.setId(1L);
    // Assume cardNetwork has necessary fields set as required.

    OfferDTO offer = new OfferDTO();
    offer.setTitle("Summer Sale");
    offer.setDescription("Get up to 50% off!");
    offer.setOfferType("Discount");
    offer.setDiscountPercentage(new BigDecimal("50"));
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(30));
    offer.setTermsAndConditions("Terms apply.");
    offer.setMerchant(merchant);
    offer.setCardNetwork(cardNetwork);
    offer.setSource("Website");
    

    // When
    OfferDTO result = offerService.createOffer(offer);

    // Then
    assertNotNull(result);
    assertEquals("Summer Sale", result.getTitle());
    assertEquals("Discount", result.getOfferType());
    assertEquals(new BigDecimal("50"), result.getDiscountPercentage());
    result.prePersist();
    assertNotNull(result.getStartDate());
    assertEquals(LocalDate.now().plusDays(30), result.getEndDate());
}

    @Test
void shouldUpdateOffer_happyPath() {
    // Given
    OfferDTO updatedOffer = new OfferDTO();
    updatedOffer.setId(1L);
    updatedOffer.setTitle("Updated Offer Title");
    updatedOffer.setOfferType("Discount");
    updatedOffer.setStartDate(LocalDate.now());
    updatedOffer.setEndDate(LocalDate.now().plusDays(10));
    updatedOffer.setSource("Online");
    

    // When
    OfferDTO result = offerService.updateOffer(1L, updatedOffer);

    // Then
    assertNotNull(result);
    assertEquals("Updated Offer Title", result.getTitle());
    assertEquals("Discount", result.getOfferType());
    result.prePersist();
    assertNotNull(result.getStartDate());
    assertEquals(LocalDate.now().plusDays(10), result.getEndDate());
    assertEquals("Online", result.getSource());
}

    @Test
void shouldSearchOffers_happyPath() {
    // Given
    MerchantDTO merchant = new MerchantDTO();
    merchant.setId(1L);
    merchant.setName("Merchant A");

    CardNetworkDTO cardNetwork = new CardNetworkDTO();
    cardNetwork.setId(1L);
    cardNetwork.setName("Network A");

    OfferDTO offer = new OfferDTO();
    offer.setTitle("Discount Offer");
    offer.setDescription("Get 20% off");
    offer.setOfferType("Discount");
    offer.setDiscountPercentage(BigDecimal.valueOf(20));
    offer.setMinimumPurchaseAmount(BigDecimal.valueOf(50));
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(10));
    offer.setTermsAndConditions("Terms apply");
    offer.setMerchant(merchant);
    offer.setCardNetwork(cardNetwork);
    offer.setSource("Website");
    offer.setMaxRedemptions(100);
    offer.setCurrentRedemptions(0);
    offer.setActive(true);
    

    // When
    List<OfferDTO> result = offerService.searchOffers("Discount", "Discount", null);

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Discount Offer", result.get(0).getTitle());
    assertEquals("Get 20% off", result.get(0).getDescription());
    assertNotNull(result.get(0).getActive());
    assertTrue(result.get(0).getActive());
}

    @Test
void shouldGetOfferById_nullId() {
    // Given
    Long id = null;

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> offerService.getOfferById(id));
}

    @Test
void shouldGetAllActiveOffers_emptyList() {
    // Given
    // No active offers present, so no setup is needed for mocks

    // When
    List<OfferDTO> result = offerService.getAllActiveOffers();

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
}



}