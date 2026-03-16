package com.cardoffers.oms.service;

import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

@SpringBootTest(exclude = {RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class, CacheAutoConfiguration.class})
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.cache.type=none"})
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
    MerchantDTO merchantDTO = new MerchantDTO();
    merchantDTO.setName("Test Merchant");
    merchantDTO.setActive(true);
    Merchant savedMerchant = merchantRepository.save(new Merchant("Test Merchant", true));

    CardNetworkDTO cardNetworkDTO = new CardNetworkDTO();
    cardNetworkDTO.setCode("VISA");
    CardNetwork savedCardNetwork = cardNetworkRepository.save(new CardNetwork("VISA"));

    OfferDTO offerDTO = new OfferDTO();
    offerDTO.setTitle("Special Offer");
    offerDTO.setOfferType("Discount");
    offerDTO.setStartDate(LocalDate.now());
    offerDTO.setEndDate(LocalDate.now().plusDays(10));
    offerDTO.setMerchant(merchantDTO);
    offerDTO.setCardNetwork(cardNetworkDTO);
    offerDTO.setSource("Website");


    // When
    OfferDTO result = offerService.createOffer(offerDTO);

    // Then
    assertNotNull(result);
    assertEquals("Special Offer", result.getTitle());
    assertEquals("Discount", result.getOfferType());
    assertEquals("Website", result.getSource());
}

    @Test
void shouldCreateOffer_notFound() {
    // Given
    OfferDTO offerDTO = new OfferDTO();
    offerDTO.setTitle("Sample Offer");
    offerDTO.setOfferType("Discount");
    offerDTO.setStartDate(LocalDate.now());
    offerDTO.setEndDate(LocalDate.now().plusDays(10));
    offerDTO.setMerchant(new MerchantDTO());
    offerDTO.setCardNetwork(new CardNetworkDTO());
    

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> offerService.createOffer(offerDTO));
}

    @Test
void shouldUpdateOffer_happyPath() {
    // Given
    Merchant merchant = new Merchant();
    merchant.setName("Test Merchant");
    merchant.setCategory("Retail");
    Merchant savedMerchant = merchantRepository.save(merchant);

    CardNetwork cardNetwork = new CardNetwork();
    cardNetwork.setCode("Visa");
    CardNetwork savedCardNetwork = cardNetworkRepository.save(cardNetwork);

    OfferDTO offerDTO = new OfferDTO();
    offerDTO.setTitle("Spring Sale");
    offerDTO.setOfferType("Discount");
    offerDTO.setStartDate(LocalDate.now());
    offerDTO.setEndDate(LocalDate.now().plusDays(10));
    offerDTO.setMerchant(new MerchantDTO(savedMerchant.getId(), savedMerchant.getName()));
    offerDTO.setCardNetwork(new CardNetworkDTO(savedCardNetwork.getId(), savedCardNetwork.getCode()));
    offerDTO.setSource("Website");
    
    Offer originalOffer = new Offer();
    originalOffer.setId(1L);
    originalOffer.setTitle("Winter Sale");
    originalOffer.setStartDate(LocalDate.now().minusDays(1));
    originalOffer.setEndDate(LocalDate.now().plusDays(1));
    originalOffer.setMerchant(savedMerchant);
    originalOffer.setCardNetwork(savedCardNetwork);
    originalOffer.setCreatedAt(java.time.LocalDateTime.now());
    originalOffer.setUpdatedAt(java.time.LocalDateTime.now());
    offerRepository.save(originalOffer);
    
    OfferDTO updatedOfferDTO = new OfferDTO();
    updatedOfferDTO.setId(originalOffer.getId());
    updatedOfferDTO.setTitle("Spring Sale");
    updatedOfferDTO.setOfferType("Discount");
    updatedOfferDTO.setStartDate(LocalDate.now());
    updatedOfferDTO.setEndDate(LocalDate.now().plusDays(10));
    updatedOfferDTO.setMerchant(new MerchantDTO(savedMerchant.getId(), savedMerchant.getName()));
    updatedOfferDTO.setCardNetwork(new CardNetworkDTO(savedCardNetwork.getId(), savedCardNetwork.getCode()));
    updatedOfferDTO.setSource("Website");


    // When
    OfferDTO result = offerService.updateOffer(originalOffer.getId(), updatedOfferDTO);

    // Then
    assertNotNull(result);
    assertEquals("Spring Sale", result.getTitle());
    assertEquals("Discount", result.getOfferType());
    assertNotNull(result.getStartDate());
    assertEquals(LocalDate.now().plusDays(10), result.getEndDate());
}

    @Test
void shouldUpdateOffer_notFound() {
    // Given
    Long nonExistentId = 99L;
    OfferDTO offerDTO = new OfferDTO();
    offerDTO.setTitle("New Offer");
    offerDTO.setOfferType("Discount");
    offerDTO.setStartDate(LocalDate.now());
    offerDTO.setEndDate(LocalDate.now().plusDays(10));
    

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
    merchant.setCategory("Retail");
    merchant.setActive(true);
    Merchant savedMerchant = merchantRepository.save(merchant);
    
    CardNetwork cardNetwork = new CardNetwork();
    cardNetwork.setCode("TEST_NET");
    cardNetwork.setActive(true);
    CardNetwork savedCardNetwork = cardNetworkRepository.save(cardNetwork);
    
    Offer offer = new Offer();
    offer.setTitle("Test Offer");
    offer.setOfferType("Discount");
    offer.setDiscountPercentage(BigDecimal.valueOf(10));
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(10));
    offer.setMerchant(savedMerchant);
    offer.setCardNetwork(savedCardNetwork);
    offer.setActive(true);
    offer.setCreatedAt(java.time.LocalDateTime.now());
    offer.setUpdatedAt(java.time.LocalDateTime.now());
    offerRepository.save(offer);
    
    List<Offer> activeOffers = new ArrayList<>();
    activeOffers.add(offer);

    // When
    List<OfferDTO> result = offerService.searchOffers("Test", "Discount", null);

    // Then
    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals("Test Offer", result.get(0).getTitle());
}

    @Test
void shouldSearchOffers_notFound() {
    // Given

    // When & Then
    assertThrows(InvalidOfferException.class, () -> offerService.searchOffers("non-existent-keyword", "anyType", "anyCategory"));
}

}