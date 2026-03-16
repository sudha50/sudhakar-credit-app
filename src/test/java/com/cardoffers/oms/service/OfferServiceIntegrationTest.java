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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.*;
import com.cardoffers.oms.model.dto.CardNetworkDTO;
import com.cardoffers.oms.model.dto.MerchantDTO;

@SpringBootTest(exclude = {RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class, CacheAutoConfiguration.class})
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.cache.type=none"})
class OfferServiceIntegrationTest {

    @Autowired
    private OfferService offerService;

    @Test
void shouldCreateOffer_happyPath() {
    // Given
    OfferDTO offerDTO = new OfferDTO();
    offerDTO.setTitle("Super Discount");
    offerDTO.setDescription("Get a great deal on your purchases.");
    offerDTO.setOfferType("Cashback");
    offerDTO.setDiscountPercentage(new BigDecimal("10.0"));
    offerDTO.setCashbackAmount(new BigDecimal("5.0"));
    offerDTO.setMinimumPurchaseAmount(new BigDecimal("50.0"));
    offerDTO.setStartDate(LocalDate.now());
    offerDTO.setEndDate(LocalDate.now().plusDays(10));
    offerDTO.setSource("Online");
    offerDTO.setMaxRedemptions(100);
    offerDTO.setCurrentRedemptions(0);
    offerDTO.setActive(true);

    MerchantDTO merchantDTO = new MerchantDTO();
    // Set required fields for MerchantDTO here
    offerDTO.setMerchant(merchantDTO);

    CardNetworkDTO cardNetworkDTO = new CardNetworkDTO();
    // Set required fields for CardNetworkDTO here
    offerDTO.setCardNetwork(cardNetworkDTO);

    // When
    OfferDTO result = offerService.createOffer(offerDTO);

    // Then
    assertNotNull(result);
    assertNotNull(result.getId());
    assertEquals("Super Discount", result.getTitle());
    assertEquals("Get a great deal on your purchases.", result.getDescription());
    assertEquals("Cashback", result.getOfferType());
    assertEquals(new BigDecimal("10.0"), result.getDiscountPercentage());
    assertEquals(new BigDecimal("5.0"), result.getCashbackAmount());
    assertEquals(new BigDecimal("50.0"), result.getMinimumPurchaseAmount());
    result.prePersist();
    assertNotNull(result.getStartDate());
    assertEquals(LocalDate.now().plusDays(10), result.getEndDate());
    assertEquals("Online", result.getSource());
    assertEquals(Integer.valueOf(100), result.getMaxRedemptions());
    assertEquals(Integer.valueOf(0), result.getCurrentRedemptions());
    assertNotNull(result.getActive());
    assertTrue(result.getActive());
}

    @Test
void shouldUpdateOffer_happyPath() {
    // Given
    OfferDTO updatedOffer = new OfferDTO();
    updatedOffer.setId(this.offer.getId());
    updatedOffer.setTitle("Updated Title");
    updatedOffer.setOfferType("Discount");
    updatedOffer.setStartDate(LocalDate.now());
    updatedOffer.setEndDate(LocalDate.now().plusDays(30));
    updatedOffer.setMerchant(this.offer.getMerchant());
    updatedOffer.setCardNetwork(this.offer.getCardNetwork());
    
    // When
    OfferDTO result = offerService.updateOffer(this.offer.getId(), updatedOffer);
    
    // Then
    assertNotNull(result);
    assertEquals("Updated Title", result.getTitle());
    assertEquals("Discount", result.getOfferType());
    assertEquals(this.offer.getId(), result.getId());
}

    @Test
void shouldSearchOffers_happyPath() {
    // Given
    MerchantDTO merchant = new MerchantDTO();
    merchant.setId(1L);
    merchant.setName("Test Merchant");

    CardNetworkDTO cardNetwork = new CardNetworkDTO();
    cardNetwork.setId(1L);
    cardNetwork.setName("Visa");

    OfferDTO offer = new OfferDTO();
    offer.setTitle("Special Discount");
    offer.setOfferType("Promotion");
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(5));
    offer.setMerchant(merchant);
    offer.setCardNetwork(cardNetwork);
    offer.setSource("Website");
    
    // Save entity to database (real integration test)
    offerService.createOffer(offer);

    // When
    List<OfferDTO> result = offerService.searchOffers("Discount", "Promotion", "All");

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Special Discount", result.get(0).getTitle());
    assertEquals("Promotion", result.get(0).getOfferType());
    assertEquals(merchant.getId(), result.get(0).getMerchant().getId());
    assertEquals(cardNetwork.getId(), result.get(0).getCardNetwork().getId());
}

    @Test
void shouldGetOfferById_nullId() {
    // Given
    Long id = null;

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> offerService.getOfferById(id));
}

    @Test
void shouldGetAllActiveOffers_emptyResult() {
    // Given
    // No active offers in the system

    // When
    List<OfferDTO> result = offerService.getAllActiveOffers();

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
}

    @Test
void shouldCreateOffer_nullDto() {
    // Given
    OfferDTO dto = null;

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> offerService.createOffer(dto));
}

}