package com.cardoffers.oms.service;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import com.cardoffers.oms.model.dto.CardNetworkDTO;
import com.cardoffers.oms.model.dto.MerchantDTO;

@SpringBootTest(exclude = {RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class, CacheAutoConfiguration.class})
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.cache.type=none"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class OfferServiceImplFunctionalTest {

    @MockBean
    private OfferRepository offerRepository;

    @MockBean
    private MerchantRepository merchantRepository;

    @MockBean
    private CardNetworkRepository cardNetworkRepository;

    @MockBean
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
    // Set other required fields for MerchantDTO

    CardNetworkDTO cardNetworkDTO = new CardNetworkDTO();
    cardNetworkDTO.setCode("TestNetwork");
    // Set other required fields for CardNetworkDTO

    OfferDTO offerDTO = new OfferDTO();
    offerDTO.setTitle("Discount Offer");
    offerDTO.setOfferType("Discount");
    offerDTO.setStartDate(LocalDate.now());
    offerDTO.setEndDate(LocalDate.now().plusDays(10));
    offerDTO.setMerchant(merchantDTO);
    offerDTO.setCardNetwork(cardNetworkDTO);
    offerDTO.setSource("Web");
    // Set other required fields for OfferDTO

    when(merchantRepository.findByNameIgnoreCase("Test Merchant")).thenReturn(Optional.of(new Merchant()));
    when(cardNetworkRepository.findByCode("TestNetwork")).thenReturn(Optional.of(new CardNetwork()));
    when(offerMapper.toEntity(any(OfferDTO.class))).thenReturn(new Offer());
    when(offerRepository.save(any(Offer.class))).thenReturn(new Offer());
    when(offerMapper.toDTO(any(Offer.class))).thenReturn(offerDTO);

    // When
    OfferDTO result = offerService.createOffer(offerDTO);

    // Then
    assertNotNull(result);
    assertEquals("Discount Offer", result.getTitle());
    assertEquals("Discount", result.getOfferType());
    assertEquals("Web", result.getSource());
    assertNotNull(result.getMerchant());
    assertNotNull(result.getCardNetwork());
}

    @Test
void shouldCreateOffer_notFound() {
    // Given
    when(offerRepository.findActiveOffers(any())).thenReturn(Collections.emptyList());
    when(merchantRepository.findByActiveTrue()).thenReturn(Collections.emptyList());
    when(cardNetworkRepository.findByActiveTrue()).thenReturn(Collections.emptyList());
    when(offerMapper.toEntity(any())).thenReturn(new Offer());
    
    OfferDTO offerDTO = new OfferDTO();
    offerDTO.setTitle("Special Offer");
    offerDTO.setOfferType("Discount");
    offerDTO.setStartDate(LocalDate.now());
    offerDTO.setEndDate(LocalDate.now().plusDays(10));
    
    // When / Then
    assertThrows(ResourceNotFoundException.class, () -> offerService.createOffer(offerDTO));
}

    @Test
void shouldUpdateOffer_happyPath() {
    // Given
    Merchant merchant = new Merchant();
    merchant.setName("Test Merchant");
    merchant.setCategory("Test Category");
    merchant.setActive(true);
    Merchant savedMerchant = merchantRepository.save(merchant);
    
    CardNetwork cardNetwork = new CardNetwork();
    cardNetwork.setCode("TEST_CODE");
    cardNetwork.setActive(true);
    CardNetwork savedCardNetwork = cardNetworkRepository.save(cardNetwork);
    
    OfferDTO offerDTO = new OfferDTO();
    offerDTO.setTitle("Test Offer");
    offerDTO.setOfferType("Discount");
    offerDTO.setStartDate(LocalDate.now());
    offerDTO.setEndDate(LocalDate.now().plusDays(10));
    offerDTO.setMerchant(new MerchantDTO(savedMerchant.getId(), savedMerchant.getName()));
    offerDTO.setCardNetwork(new CardNetworkDTO(savedCardNetwork.getCode(), true));
    offerDTO.setSource("Online");

    Offer offer = new Offer();
    offer.setId(1L);
    offer.setTitle("Old Offer");
    offer.setMerchant(savedMerchant);
    offer.setCardNetwork(savedCardNetwork);
    when(offerRepository.findById(1L)).thenReturn(Optional.of(offer));
    
    offer.setTitle(offerDTO.getTitle());
    Offer updatedOffer = new Offer();
    updatedOffer.setId(1L);
    updatedOffer.setTitle(offerDTO.getTitle());
    updatedOffer.setMerchant(savedMerchant);
    updatedOffer.setCardNetwork(savedCardNetwork);    
    when(offerRepository.save(any())).thenReturn(updatedOffer);
    when(offerMapper.toDTO(any())).thenReturn(offerDTO);

    // When
    OfferDTO result = offerService.updateOffer(1L, offerDTO);

    // Then
    assertNotNull(result);
    assertEquals("Test Offer", result.getTitle());
    verify(offerRepository).findById(1L);
    verify(offerRepository).save(any());
}

    @Test
void shouldUpdateOffer_notFound() {
    // Given
    when(offerRepository.findActiveOffersByMerchant(anyLong())).thenReturn(Collections.emptyList());
    when(merchantRepository.findByActiveTrue()).thenReturn(Collections.emptyList());
    when(cardNetworkRepository.findByActiveTrue()).thenReturn(Collections.emptyList());
    when(offerMapper.toEntity(any())).thenReturn(new Offer());
    when(offerRepository.findById(anyLong())).thenReturn(Optional.empty());

    // When & Then
    assertThrows(ResourceNotFoundException.class, () ->        // CRITICAL: Read-Modify-Write pattern required.
        // Save the complete entity FIRST (all @NotNull fields), then call update.
        // The service should load the existing entity from DB before updating to preserve createdAt.
        // Pattern: EntityType saved = repository.save(new EntityType(...all fields...));
        //           UpdateDTO dto = new UpdateDTO(saved.getId(), "new value");
        //           service.update(dto); // createdAt preserved via DB read
 offerService.updateOffer(1L, new OfferDTO()));
}

    @Test
void shouldSearchOffers_happyPath() {
    // Given
    Merchant merchant = new Merchant();
    merchant.setName("Test Merchant");
    // ... set other required fields
    merchantRepository.save(merchant);
    
    CardNetwork cardNetwork = new CardNetwork();
    cardNetwork.setCode("TEST_CODE");
    // ... set other required fields
    cardNetworkRepository.save(cardNetwork);
    
    Offer offer = new Offer();
    offer.setTitle("Test Offer");
    offer.setOfferType("Test Type");
    // ... set other required fields
    offer.setMerchant(merchant);
    offer.setCardNetwork(cardNetwork);
    offerRepository.save(offer);
    
    when(offerRepository.findActiveOffers(any())).thenReturn(Collections.singletonList(offer));
    when(offerMapper.toDTO(any())).thenReturn(new OfferDTO(/*... set fields from offer*/));

    // When
    List<OfferDTO> result = offerService.searchOffers("Test", "Test Type", "Test Category");

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Test Offer", result.get(0).getTitle());
    assertEquals("Test Type", result.get(0).getOfferType());
}

    @Test
void shouldSearchOffers_notFound() {
    // Given
    when(offerRepository.findActiveOffersByMerchant(anyLong())).thenReturn(Collections.emptyList());
    when(merchantRepository.findByActiveTrue()).thenReturn(Collections.emptyList());
    when(cardNetworkRepository.findByActiveTrue()).thenReturn(Collections.emptyList());
    when(offerMapper.toSummaryDTO(any())).thenReturn(new OfferSummaryDTO());

    // When & Then
    assertThrows(InvalidOfferException.class, () -> offerService.searchOffers("nonexistent", "type", "category"));
}

}