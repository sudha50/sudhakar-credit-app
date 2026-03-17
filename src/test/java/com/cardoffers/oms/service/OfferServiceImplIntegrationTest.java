package com.cardoffers.oms.service;

import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import com.cardoffers.oms.exception.InvalidOfferException;
import com.cardoffers.oms.exception.ResourceNotFoundException;
import com.cardoffers.oms.mapper.OfferMapper;
import com.cardoffers.oms.model.dto.OfferDTO;
import com.cardoffers.oms.model.dto.OfferSummaryDTO;
import com.cardoffers.oms.model.entity.CardNetwork;
import com.cardoffers.oms.model.entity.Merchant;
import com.cardoffers.oms.model.entity.Offer;
import com.cardoffers.oms.repository.CardNetworkRepository;
import com.cardoffers.oms.repository.MerchantRepository;
import com.cardoffers.oms.repository.OfferRepository;
import com.cardoffers.oms.service.OfferService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
class OfferServiceImplIntegrationTest {

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private CardNetworkRepository cardNetworkRepository;

    @Autowired
    private OfferMapper offerMapper;

    @Autowired
    private OfferService offerService;

    @BeforeEach
    void setUp() {
        // P16-B2 / S-27: Topological save strategy — parents MUST be saved before children
        // Save order: Merchant → CardNetwork → Offer
        // CRITICAL: this.offer is a class-level instance field.
        // Test methods MUST use this.offer — do NOT create a new Offer() inline.

        Merchant merchant = new Merchant();
        merchant.setName("testName");
        merchant.setCategory("testCategory");
        merchant.setCreatedAt(java.time.LocalDateTime.now());
        merchant = merchantRepository.save(merchant);

        CardNetwork cardNetwork = new CardNetwork();
        cardNetwork.setName("testName");
        cardNetwork.setCode("testCode");
        cardNetwork.setCreatedAt(java.time.LocalDateTime.now());
        cardNetwork = cardNetworkRepository.save(cardNetwork);

        Offer offer = new Offer();
        offer.setTitle("testTitle");
        offer.setCreatedAt(java.time.LocalDateTime.now());
        offer.setUpdatedAt(java.time.LocalDateTime.now());
        offer.setMerchant(merchant); // P16-B2: transitive FK chaining
        offer.setCardNetwork(cardNetwork); // P16-B2: transitive FK chaining
        this.offer = offerRepository.save(offer);
    }

    @Test
void shouldCreateOffer_happyPath() {
    // Given
    Merchant merchant = new Merchant();
    merchant.setName("Test Merchant");

    CardNetwork cardNetwork = new CardNetwork();
    cardNetwork.setCode("VISA");
    
    OfferDTO offerDTO = new OfferDTO();
    offerDTO.setTitle("Test Offer");
    offerDTO.setOfferType("Discount");
    offerDTO.setStartDate(LocalDate.now());
    offerDTO.setEndDate(LocalDate.now().plusDays(10));
    offerDTO.setMerchant(new MerchantDTO());
    offerDTO.getMerchant().setName("Test Merchant");
    offerDTO.setCardNetwork(new CardNetworkDTO());
    offerDTO.getCardNetwork().setCode("VISA");
    

    // When
    OfferDTO result = offerService.createOffer(offerDTO);

    // Then
    assertNotNull(result);
    assertEquals("Test Offer", result.getTitle());
    assertEquals("Discount", result.getOfferType());
    assertEquals("Test Merchant", result.getMerchant().getName());
    assertEquals("VISA", result.getCardNetwork().getCode());
}

    @Test
void shouldCreateOffer_notFound() {
    // Given
    OfferDTO offerDTO = new OfferDTO();
    offerDTO.setTitle("Special Offer");
    offerDTO.setOfferType("Discount");
    offerDTO.setStartDate(LocalDate.now());
    offerDTO.setEndDate(LocalDate.now().plusDays(30));
    offerDTO.setSource("Marketing");
    
    
    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> offerService.createOffer(offerDTO));
}

    @Test
void shouldUpdateOffer_happyPath() {
    // Given
    Merchant merchant = new Merchant();
    merchant.setId(1L);
    merchant.setName("Test Merchant");
    
    CardNetwork cardNetwork = new CardNetwork();
    cardNetwork.setId(1L);
    cardNetwork.setCode("VISA");
    
    Offer offer = new Offer();
    offer.setId(1L);
    offer.setTitle("Old Offer");
    offer.setMerchant(merchant);
    offer.setCardNetwork(cardNetwork);
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(10));
    
    
    OfferDTO updatedOfferDTO = new OfferDTO();
    updatedOfferDTO.setId(1L);
    updatedOfferDTO.setTitle("Updated Offer");
    updatedOfferDTO.setMerchant(new MerchantDTO());
    updatedOfferDTO.setCardNetwork(new CardNetworkDTO());
    updatedOfferDTO.setStartDate(LocalDate.now());
    updatedOfferDTO.setEndDate(LocalDate.now().plusDays(10));
    

    // When
    OfferDTO result = offerService.updateOffer(1L, updatedOfferDTO);
    
    // Then
    assertNotNull(result);
    assertEquals("Updated Offer", result.getTitle());
}

    @Test
void shouldUpdateOffer_notFound() {
    // Given
    Long nonExistentId = 1L;
    OfferDTO offerDTO = new OfferDTO();
    offerDTO.setTitle("Summer Sale");
    offerDTO.setOfferType("Discount");
    offerDTO.setSource("Website");
    
    
    // When & Then
    assertThrows(ResourceNotFoundException.class, () ->        // CRITICAL: Read-Modify-Write pattern required.
        // Save the complete entity FIRST (all @NotNull fields), then call update.
        // The service should load the existing entity from DB before updating to preserve createdAt.
        // Pattern: EntityType saved = repository.save(new EntityType(...all fields...));
        //           UpdateDTO dto = new UpdateDTO(saved.getId(), "new value");
        //           service.update(dto); // createdAt preserved via DB read
 offerService.updateOffer(nonExistentId, offerDTO));
}

    @Test
void shouldSearchOffers_happyPath() {
    // Given
    Merchant merchant = new Merchant();
    merchant.setName("Test Merchant");
    merchant.setCategory("Test Category");
    merchant.setEmail("merchant@example.com");
    merchant.setActive(true);
    Merchant savedMerchant = merchantRepository.save(merchant);

    CardNetwork cardNetwork = new CardNetwork();
    cardNetwork.setCode("VISA");
    cardNetwork.setActive(true);
    CardNetwork savedCardNetwork = cardNetworkRepository.save(cardNetwork);

    Offer offer = new Offer();
    offer.setTitle("Special Discount");
    offer.setDescription("Get 20% off");
    offer.setOfferType("Discount");
    offer.setDiscountPercentage(new BigDecimal("20.00"));
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(30));
    offer.setMerchant(savedMerchant);
    offer.setCardNetwork(savedCardNetwork);
    
    offerRepository.save(offer);


    // When
    List<OfferDTO> result = offerService.searchOffers("Discount", null, null);

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Special Discount", result.get(0).getTitle());
}

    @Test
void shouldSearchOffers_notFound() {
    // Given

    // When & Then
    assertThrows(InvalidOfferException.class, () -> offerService.searchOffers("nonExistentKeyword", "anyType", "anyCategory"));
}

}