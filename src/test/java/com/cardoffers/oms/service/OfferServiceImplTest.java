package com.cardoffers.oms.service;

import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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
    merchantDTO.setName("Merchant A");
    when(merchantRepository.findByNameIgnoreCase("Merchant A")).thenReturn(Optional.of(new Merchant()));

    CardNetworkDTO cardNetworkDTO = new CardNetworkDTO();
    cardNetworkDTO.setCode("Visa");
    when(cardNetworkRepository.findByCode("Visa")).thenReturn(Optional.of(new CardNetwork()));

    OfferDTO offerDTO = new OfferDTO();
    offerDTO.setTitle("Special Offer");
    offerDTO.setOfferType("Discount");
    offerDTO.setStartDate(LocalDate.now());
    offerDTO.setEndDate(LocalDate.now().plusDays(10));
    offerDTO.setMerchant(merchantDTO);
    offerDTO.setCardNetwork(cardNetworkDTO);
    when(offerMapper.toEntity(offerDTO)).thenReturn(new Offer());
    when(offerRepository.save(any())).thenReturn(new Offer());
    when(offerMapper.toDTO(any())).thenReturn(offerDTO);

    // When
    OfferDTO result = offerService.createOffer(offerDTO);

    // Then
    assertNotNull(result);
    assertEquals("Special Offer", result.getTitle());
    assertEquals("Discount", result.getOfferType());
    assertEquals("Merchant A", result.getMerchant().getName());
    assertEquals("Visa", result.getCardNetwork().getCode());
}

    @Test
void shouldCreateOffer_notFound() {
    // Given
    OfferDTO offerDTO = new OfferDTO();
    offerDTO.setTitle("Special Offer");
    offerDTO.setOfferType("Discount");
    offerDTO.setStartDate(LocalDate.now());
    offerDTO.setEndDate(LocalDate.now().plusDays(30));
    offerDTO.setSource("Website");

    when(merchantRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());
    when(cardNetworkRepository.findByCode(anyString())).thenReturn(Optional.empty());
    when(offerMapper.toEntity(any())).thenReturn(new Offer());

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> offerService.createOffer(offerDTO));
}

    @Test
void shouldUpdateOffer_happyPath() {
    // Given
    Long offerId = 1L;
    OfferDTO offerDTO = new OfferDTO();
    offerDTO.setId(offerId);
    offerDTO.setTitle("Discount Offer");
    offerDTO.setOfferType("Discount");
    offerDTO.setStartDate(LocalDate.now());
    offerDTO.setEndDate(LocalDate.now().plusDays(10));
    offerDTO.setSource("Website");
    
    Offer existingOffer = new Offer();
    existingOffer.setId(offerId);
    existingOffer.setTitle("Old Offer");
    existingOffer.setOfferType("Discount");
    existingOffer.setStartDate(LocalDate.now());
    existingOffer.setEndDate(LocalDate.now().plusDays(10));
    
    Offer updatedOffer = new Offer();
    updatedOffer.setId(offerId);
    updatedOffer.setTitle("Discount Offer");
    updatedOffer.setOfferType("Discount");
    updatedOffer.setStartDate(LocalDate.now());
    updatedOffer.setEndDate(LocalDate.now().plusDays(10));

    when(offerRepository.findById(offerId)).thenReturn(Optional.of(existingOffer));
    when(offerRepository.save(any())).thenReturn(updatedOffer);
    when(offerMapper.toEntity(offerDTO)).thenReturn(updatedOffer);
    when(offerMapper.toDTO(updatedOffer)).thenReturn(offerDTO);

    // When
    OfferDTO result = offerService.updateOffer(offerId, offerDTO);

    // Then
    assertNotNull(result);
    assertEquals("Discount Offer", result.getTitle());
    assertEquals(offerId, result.getId());
    verify(offerRepository).findById(offerId);
    verify(offerRepository).save(any());
    verify(offerMapper).toEntity(offerDTO);
    verify(offerMapper).toDTO(updatedOffer);
}

    @Test
void shouldUpdateOffer_notFound() {
    // Given
    Long offerId = 1L;
    OfferDTO offerDTO = new OfferDTO();
    offerDTO.setTitle("Special Discount");
    when(offerRepository.findById(anyLong())).thenReturn(Optional.empty());
    when(merchantRepository.findByActiveTrue()).thenReturn(Collections.emptyList());
    when(cardNetworkRepository.findByActiveTrue()).thenReturn(Collections.emptyList());
    when(offerMapper.toEntity(any())).thenReturn(new Offer());

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> offerService.updateOffer(offerId, offerDTO));
    verify(offerRepository).findById(offerId);
}

    @Test
void shouldSearchOffers_happyPath() {
    // Given
    Merchant merchant = new Merchant();
    merchant.setName("Merchant A");
    merchant.setCategory("Category A");
    merchant.setEmail("merchantA@example.com");
    merchant.setCode("M001");
    merchant.setCreatedAt(LocalDate.now());
    merchantRepository.save(merchant);

    CardNetwork cardNetwork = new CardNetwork();
    cardNetwork.setCode("VISA");
    cardNetwork.setActive(true);
    cardNetwork.setName("name");
    cardNetwork.setCreatedAt(java.time.LocalDateTime.now());
    cardNetworkRepository.save(cardNetwork);

    Offer offer = new Offer();
    offer.setTitle("Limited Time Offer");
    offer.setDescription("Get 20% off");
    offer.setOfferType("DISCOUNT");
    offer.setDiscountPercentage(new BigDecimal("20"));
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(10));
    offer.setMerchant(merchant);
    offer.setCardNetwork(cardNetwork);
    offer.setCreatedAt(java.time.LocalDateTime.now());
    offer.setUpdatedAt(java.time.LocalDateTime.now());
    offerRepository.save(offer);
    
    List<Offer> offers = new ArrayList<>();
    offers.add(offer);
    
    when(offerRepository.findActiveOffers(any())).thenReturn(offers);
    when(offerMapper.toDTO(any(Offer.class))).thenAnswer(invocation -> {
        Offer entity = invocation.getArgument(0);
        OfferDTO dto = new OfferDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setOfferType(entity.getOfferType());
        dto.setDiscountPercentage(entity.getDiscountPercentage());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setMerchant(new MerchantDTO()); // Assuming MerchantDTO has a no-arg constructor
        dto.setCardNetwork(new CardNetworkDTO()); // Assuming CardNetworkDTO has a no-arg constructor
        return dto;
    });

    // When
    List<OfferDTO> result = offerService.searchOffers("Limited", "DISCOUNT", "Category A");

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Limited Time Offer", result.get(0).getTitle());
    assertEquals("DISCOUNT", result.get(0).getOfferType());
    assertEquals(new BigDecimal("20"), result.get(0).getDiscountPercentage());
    verify(offerRepository).findActiveOffers(any());
    verify(offerMapper).toDTO(any(Offer.class));
}

    @Test
void shouldSearchOffers_notFound() {
    // Given
    when(offerRepository.findActiveOffers(any())).thenReturn(Collections.emptyList());
    when(merchantRepository.findByActiveTrue()).thenReturn(Collections.emptyList());
    when(cardNetworkRepository.findByActiveTrue()).thenReturn(Collections.emptyList());
    when(offerMapper.toDTO(any())).thenReturn(new OfferDTO());

    // When & Then
    assertThrows(InvalidOfferException.class, () -> offerService.searchOffers("nonexistent", "discount", "electronics"));
}

    @Test
void shouldGetOfferById_nullId() {
    // Given
    Long id = null;

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> offerService.getOfferById(id));
}

    @Test
void shouldGetOfferById_emptyResponse() {
    // Given
    Long nonExistentId = 999L;
    when(offerRepository.findById(nonExistentId)).thenReturn(Optional.empty());

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> offerService.getOfferById(nonExistentId));
    verify(offerRepository).findById(nonExistentId);
}

}