package com.cardoffers.oms.service;

import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
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
import com.cardoffers.oms.service.OfferServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import com.cardoffers.oms.model.dto.CardNetworkDTO;
import com.cardoffers.oms.model.dto.MerchantDTO;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class OfferServiceImplTest {

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private MerchantRepository merchantRepository;

    @Mock
    private CardNetworkRepository cardNetworkRepository;

    @Mock
    private OfferMapper offerMapper;

    @InjectMocks
    private OfferServiceImpl offerService;

    @Test
void shouldCreateOffer_happyPath() {
    // Given
    MerchantDTO merchantDTO = new MerchantDTO();
    merchantDTO.setName("Test Merchant");
    when(merchantRepository.findByNameIgnoreCase("Test Merchant")).thenReturn(Optional.of(new Merchant()));

    CardNetworkDTO cardNetworkDTO = new CardNetworkDTO();
    cardNetworkDTO.setCode("TEST_CODE");
    when(cardNetworkRepository.findByCode("TEST_CODE")).thenReturn(Optional.of(new CardNetwork()));

    OfferDTO offerDTO = new OfferDTO();
    offerDTO.setTitle("Test Offer");
    offerDTO.setOfferType("Discount");
    offerDTO.setStartDate(LocalDate.now());
    offerDTO.setEndDate(LocalDate.now().plusDays(30));
    offerDTO.setMerchant(merchantDTO);
    offerDTO.setCardNetwork(cardNetworkDTO);
    when(offerMapper.toEntity(offerDTO)).thenReturn(new Offer());
    when(offerRepository.save(any())).thenReturn(new Offer());
    when(offerMapper.toDTO(any())).thenReturn(offerDTO);

    // When
    OfferDTO result = offerService.createOffer(offerDTO);

    // Then
    assertNotNull(result);
    assertEquals("Test Offer", result.getTitle());
    assertEquals("Discount", result.getOfferType());
    assertEquals("Test Merchant", result.getMerchant().getName());
    assertEquals("TEST_CODE", result.getCardNetwork().getCode());
}

    @Test
void shouldCreateOffer_notFound() {
    // Given
    OfferDTO offerDTO = new OfferDTO();
    offerDTO.setTitle("Special Discount");
    offerDTO.setOfferType("Discount");
    offerDTO.setStartDate(LocalDate.now());
    offerDTO.setEndDate(LocalDate.now().plusDays(10));
    offerDTO.setSource("Online");
    offerDTO.setMerchant(new MerchantDTO());
    offerDTO.setCardNetwork(new CardNetworkDTO());

    when(offerRepository.findActiveOffers(any())).thenReturn(new ArrayList<>());
    when(merchantRepository.findByActiveTrue()).thenReturn(new ArrayList<>());
    when(cardNetworkRepository.findByActiveTrue()).thenReturn(new ArrayList<>());
    when(offerMapper.toEntity(any())).thenReturn(new Offer());

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> offerService.createOffer(offerDTO));
}

    @Test
void shouldUpdateOffer_happyPath() {
    // Given
    Merchant merchant = new Merchant();
    merchant.setName("Test Merchant");
    merchant.setActive(true);
    Merchant savedMerchant = merchantRepository.save(merchant);

    CardNetwork cardNetwork = new CardNetwork();
    cardNetwork.setCode("Visa");
    CardNetwork savedCardNetwork = cardNetworkRepository.save(cardNetwork);

    OfferDTO offerDTO = new OfferDTO();
    offerDTO.setTitle("Summer Sale");
    offerDTO.setOfferType("Discount");
    offerDTO.setStartDate(LocalDate.now());
    offerDTO.setEndDate(LocalDate.now().plusDays(30));
    offerDTO.setMerchant(new MerchantDTO());
    offerDTO.getMerchant().setId(savedMerchant.getId());
    offerDTO.setCardNetwork(new CardNetworkDTO());
    offerDTO.getCardNetwork().setId(savedCardNetwork.getId());

    Offer offer = new Offer();
    offer.setId(1L);
    offer.setTitle("Old Title");
    offer.setMerchant(savedMerchant);
    offer.setCardNetwork(savedCardNetwork);
    
    Offer updatedOffer = new Offer();
    updatedOffer.setId(1L);
    updatedOffer.setTitle("Summer Sale");
    updatedOffer.setMerchant(savedMerchant);
    updatedOffer.setCardNetwork(savedCardNetwork);

    when(offerRepository.findById(1L)).thenReturn(Optional.of(offer));
    when(offerRepository.save(any())).thenReturn(updatedOffer);
    when(offerMapper.toDTO(any())).thenReturn(offerDTO);

    // When
    OfferDTO result = offerService.updateOffer(1L, offerDTO);

    // Then
    assertNotNull(result);
    assertEquals("Summer Sale", result.getTitle());
    verify(offerRepository).findById(1L);
    verify(offerRepository).save(any());
    verify(offerMapper).toDTO(any());
}

    @Test
void shouldUpdateOffer_notFound() {
    // Given
    Long id = 1L;
    OfferDTO offerDTO = new OfferDTO();
    offerDTO.setTitle("New Offer");
    offerDTO.setOfferType("Discount");
    offerDTO.setStartDate(LocalDate.now());
    offerDTO.setEndDate(LocalDate.now().plusDays(10));
    offerDTO.setMerchant(new MerchantDTO()); // assuming a valid MerchantDTO
    offerDTO.setCardNetwork(new CardNetworkDTO()); // assuming a valid CardNetworkDTO

    when(offerRepository.findActiveOffers(any())).thenReturn(Collections.emptyList());
    when(merchantRepository.findByActiveTrue()).thenReturn(Collections.emptyList());
    when(cardNetworkRepository.findByActiveTrue()).thenReturn(Collections.emptyList());
    when(offerMapper.toEntity(any())).thenReturn(new Offer());

    when(offerRepository.findActiveOffersByMerchant(anyLong())).thenReturn(Collections.emptyList());
    when(offerRepository.findActiveOffersByCardNetwork(anyLong())).thenReturn(Collections.emptyList());
    when(offerRepository.updateOffer(anyLong(), any())).thenReturn(null); // This triggers the exception

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> offerService.updateOffer(id, offerDTO));
}

    @Test
void shouldSearchOffers_happyPath() {
    // Given
    Merchant merchant = new Merchant();
    merchant.setName("Test Merchant");
    merchant.setCategory("Retail");
    Merchant savedMerchant = merchantRepository.save(merchant);
    
    CardNetwork cardNetwork = new CardNetwork();
    cardNetwork.setCode("VISA");
    CardNetwork savedCardNetwork = cardNetworkRepository.save(cardNetwork);
    
    Offer offer = new Offer();
    offer.setTitle("Test Offer");
    offer.setDescription("Description of test offer");
    offer.setOfferType("Discount");
    offer.setDiscountPercentage(BigDecimal.valueOf(10));
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(30));
    offer.setMerchant(savedMerchant);
    offer.setCardNetwork(savedCardNetwork);
    offer.setSource("Web");
    offer.setActive(true);
    offer.setCreatedAt(java.time.LocalDateTime.now());
    offer.setUpdatedAt(java.time.LocalDateTime.now());
    offerRepository.save(offer);

    when(offerRepository.findActiveOffers(any())).thenReturn(List.of(offer));
    when(offerMapper.toDTO(any())).thenReturn(new OfferDTO()); // Adjust with proper DTO setup

    // When
    List<OfferDTO> result = offerService.searchOffers("Test", "Discount", "Retail");

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Test Offer", result.get(0).getTitle());
    assertEquals("Discount", result.get(0).getOfferType());
    assertEquals(true, result.get(0).getActive());
    verify(offerRepository).findActiveOffers(any());
}

    @Test
void shouldSearchOffers_notFound() {
    // Given
    when(offerRepository.findActiveOffers(any())).thenReturn(Collections.emptyList());
    when(merchantRepository.findByActiveTrue()).thenReturn(Collections.emptyList());
    when(cardNetworkRepository.findByActiveTrue()).thenReturn(Collections.emptyList());
    when(offerMapper.toDTO(any())).thenReturn(new OfferDTO());
    
    // When & Then
    assertThrows(InvalidOfferException.class, () -> offerService.searchOffers("nonExistentKeyword", "someType", "someCategory"));
}

    @Test
void shouldGetOfferById_nullId() {
    // Given
    Long id = null;

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> offerService.getOfferById(id));
}

    @Test
void shouldGetOfferById_nonExistentId() {
    // Given
    Long nonExistentId = 999L;
    when(offerRepository.findById(nonExistentId)).thenReturn(Optional.empty());

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> offerService.getOfferById(nonExistentId));
    verify(offerRepository).findById(nonExistentId);
}

}