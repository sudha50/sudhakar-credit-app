package com.cardoffers.oms.service;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.springframework.test.annotation.DirtiesContext;
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

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.cache.type=none",
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration,org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration"
    })
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
    Merchant merchant = new Merchant();
    merchant.setName("Test Merchant");
    merchant.setCategory("Retail");
    Merchant savedMerchant = merchantRepository.save(merchant);

    CardNetwork cardNetwork = new CardNetwork();
    cardNetwork.setCode("VISA");
    CardNetwork savedCardNetwork = cardNetworkRepository.save(cardNetwork);

    OfferDTO offerDTO = new OfferDTO();
    offerDTO.setTitle("Special Offer");
    offerDTO.setOfferType("Discount");
    offerDTO.setStartDate(LocalDate.now());
    offerDTO.setEndDate(LocalDate.now().plusDays(10));
    offerDTO.setMerchant(new MerchantDTO()); 
    offerDTO.getMerchant().setName(savedMerchant.getName());
    offerDTO.getMerchant().setCategory(savedMerchant.getCategory());
    offerDTO.setCardNetwork(new CardNetworkDTO());
    offerDTO.getCardNetwork().setCode(savedCardNetwork.getCode());

    Offer offerEntity = new Offer();
    offerEntity.setTitle(offerDTO.getTitle());
    offerEntity.setOfferType(offerDTO.getOfferType());
    offerEntity.setStartDate(offerDTO.getStartDate());
    offerEntity.setEndDate(offerDTO.getEndDate());
    offerEntity.setMerchant(savedMerchant);
    offerEntity.setCardNetwork(savedCardNetwork);

    when(offerMapper.toEntity(offerDTO)).thenReturn(offerEntity);
    offerEntity.setId(1L);
    when(offerRepository.save(any())).thenReturn(offerEntity);
    when(offerMapper.toDTO(offerEntity)).thenReturn(offerDTO);

    // When
    OfferDTO result = offerService.createOffer(offerDTO);

    // Then
    assertNotNull(result);
    assertEquals("Special Offer", result.getTitle());
    assertEquals("Discount", result.getOfferType());
    assertNotNull(result.getStartDate());
    assertNotNull(result.getEndDate());
    assertEquals("Test Merchant", result.getMerchant().getName());
    assertEquals("VISA", result.getCardNetwork().getCode());
}

    @Test
void shouldCreateOffer_notFound() {
    // Given
    OfferDTO offerDTO = new OfferDTO();
    offerDTO.setTitle("New Offer");
    offerDTO.setOfferType("Discount");
    offerDTO.setStartDate(LocalDate.now());
    offerDTO.setEndDate(LocalDate.now().plusDays(10));
    offerDTO.setSource("Online");

    when(merchantRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());
    when(cardNetworkRepository.findByCode(anyString())).thenReturn(Optional.of(new CardNetwork()));
    when(offerMapper.toEntity(any())).thenReturn(new Offer());
    when(offerRepository.findActiveOffers(any())).thenReturn(Collections.emptyList());

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> offerService.createOffer(offerDTO));
}

    @Test
void shouldUpdateOffer_happyPath() {
    // Given
    Merchant merchant = new Merchant();
    merchant.setName("Test Merchant");
    merchant.setCategory("Retail");
    merchant.setEmail("merchant@example.com");
    Merchant savedMerchant = merchantRepository.save(merchant);

    CardNetwork cardNetwork = new CardNetwork();
    cardNetwork.setCode("VISA");
    CardNetwork savedCardNetwork = cardNetworkRepository.save(cardNetwork);

    Offer offer = new Offer();
    offer.setId(1L);
    offer.setTitle("Original Title");
    offer.setOfferType("Discount");
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(10));
    offer.setMerchant(savedMerchant);
    offer.setCardNetwork(savedCardNetwork);
    offer.setCreatedAt(java.time.LocalDateTime.now());
    offer.setUpdatedAt(java.time.LocalDateTime.now());
    offerRepository.save(offer);
    
    OfferDTO offerDTO = new OfferDTO();
    offerDTO.setId(1L);
    offerDTO.setTitle("Updated Title");
    offerDTO.setOfferType("Discount");
    offerDTO.setStartDate(LocalDate.now());
    offerDTO.setEndDate(LocalDate.now().plusDays(10));
    offerDTO.setMerchant(new MerchantDTO()); // Assuming MerchantDTO has a no-arg constructor and setters
    offerDTO.setCardNetwork(new CardNetworkDTO()); // Assuming CardNetworkDTO has a no-arg constructor and setters

    Offer updatedOffer = new Offer();
    updatedOffer.setId(1L);
    updatedOffer.setTitle("Updated Title");
    updatedOffer.setOfferType("Discount");
    updatedOffer.setStartDate(LocalDate.now());
    updatedOffer.setEndDate(LocalDate.now().plusDays(10));
    updatedOffer.setMerchant(savedMerchant);
    updatedOffer.setCardNetwork(savedCardNetwork);
    
    when(offerRepository.findById(1L)).thenReturn(Optional.of(offer));
    when(offerRepository.save(any())).thenReturn(updatedOffer);
    when(offerMapper.toDTO(any())).thenReturn(offerDTO);

    // When
    OfferDTO result = offerService.updateOffer(1L, offerDTO);

    // Then
    assertNotNull(result);
    assertEquals("Updated Title", result.getTitle());
    verify(offerRepository).findById(1L);
    verify(offerRepository).save(any());
}

    @Test
void shouldUpdateOffer_notFound() {
    // Given
    Long offerId = 1L;
    OfferDTO offerDTO = new OfferDTO();
    offerDTO.setTitle("Special Offer");
    offerDTO.setOfferType("Discount");
    when(offerRepository.findById(anyLong())).thenReturn(Optional.empty());
    when(merchantRepository.findByActiveTrue()).thenReturn(List.of());
    when(cardNetworkRepository.findByActiveTrue()).thenReturn(List.of());
    when(offerMapper.toEntity(any())).thenReturn(new Offer());

    // When & Then
    assertThrows(ResourceNotFoundException.class, () ->        // CRITICAL: Read-Modify-Write pattern required.
        // Save the complete entity FIRST (all @NotNull fields), then call update.
        // The service should load the existing entity from DB before updating to preserve createdAt.
        // Pattern: EntityType saved = repository.save(new EntityType(...all fields...));
        //           UpdateDTO dto = new UpdateDTO(saved.getId(), "new value");
        //           service.update(dto); // createdAt preserved via DB read
 offerService.updateOffer(offerId, offerDTO));
}

    @Test
void shouldSearchOffers_happyPath() {
    // Given
    Merchant merchant = new Merchant();
    merchant.setName("Test Merchant");
    merchant.setActive(true);
    Merchant savedMerchant = merchantRepository.save(merchant);
    
    CardNetwork cardNetwork = new CardNetwork();
    cardNetwork.setCode("CREDIT");
    cardNetwork.setActive(true);
    CardNetwork savedCardNetwork = cardNetworkRepository.save(cardNetwork);
    
    Offer offer = new Offer();
    offer.setTitle("Special Offer");
    offer.setOfferType("Discount");
    offer.setDiscountPercentage(BigDecimal.valueOf(20));
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(30));
    offer.setMerchant(savedMerchant);
    offer.setCardNetwork(savedCardNetwork);
    offer.setActive(true);
    
    when(offerRepository.findActiveOffers(any())).thenReturn(List.of(offer));
    when(offerMapper.toDTO(any())).thenReturn(new OfferDTO());

    // When
    List<OfferDTO> result = offerService.searchOffers("Special", "Discount", null);

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Special Offer", result.get(0).getTitle());
    verify(offerRepository).findActiveOffers(any());
    verify(offerMapper).toDTO(any());
}

    @Test
void shouldSearchOffers_notFound() {
    // Given
    when(offerRepository.findActiveOffers(any())).thenReturn(Collections.emptyList());
    when(merchantRepository.findByActiveTrue()).thenReturn(Collections.emptyList());
    when(cardNetworkRepository.findByActiveTrue()).thenReturn(Collections.emptyList());
    when(offerMapper.toSummaryDTO(any())).thenReturn(new OfferSummaryDTO());

    // When & Then
    assertThrows(InvalidOfferException.class, () -> offerService.searchOffers("non-existent", null, null));
}

}