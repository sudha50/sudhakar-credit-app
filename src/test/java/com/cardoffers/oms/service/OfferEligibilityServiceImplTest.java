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
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import com.cardoffers.oms.exception.CardholderNotEligibleException;
import com.cardoffers.oms.exception.ResourceNotFoundException;
import com.cardoffers.oms.mapper.OfferMapper;
import com.cardoffers.oms.model.dto.EligibleOfferResponseDTO;
import com.cardoffers.oms.model.dto.OfferDTO;
import com.cardoffers.oms.model.entity.Cardholder;
import com.cardoffers.oms.model.entity.CardholderCard;
import com.cardoffers.oms.model.entity.Offer;
import com.cardoffers.oms.repository.CardholderCardRepository;
import com.cardoffers.oms.repository.CardholderRepository;
import com.cardoffers.oms.repository.OfferRepository;
import com.cardoffers.oms.service.OfferEligibilityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import com.cardoffers.oms.model.entity.Merchant;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class OfferEligibilityServiceImplTest {

    @Mock
    private CardholderRepository cardholderRepository;

    @Mock
    private CardholderCardRepository cardholderCardRepository;

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private OfferMapper offerMapper;

    @Mock
    private OfferEligibilityService offerEligibilityServiceProxy;

    @InjectMocks
    private OfferEligibilityServiceImpl offerEligibilityService;

    @Test
void shouldFilterOffersByCategory_happyPath() {
    // Given
    Long cardholderId = 1L;
    String category = "Electronics";
    Offer offer = new Offer();
    offer.setId(1L);
    offer.setTitle("Discount on TVs");
    offer.setActive(true);
    offer.setMerchant(new Merchant()); // Assuming Merchant is properly set up
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(30));
    List<Offer> offers = Collections.singletonList(offer);

    when(offerRepository.findActiveOffers(any(LocalDate.class))).thenReturn(offers);
    when(offerMapper.toDTO(any(Offer.class))).thenReturn(new OfferDTO());

    // When
    List<OfferDTO> result = offerEligibilityService.filterOffersByCategory(cardholderId, category);

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(1L, result.get(0).getId());
    verify(offerRepository).findActiveOffers(any(LocalDate.class));
    verify(offerMapper).toDTO(any(Offer.class));
}

    @Test
void shouldFilterOffersByOfferType_happyPath() {
    // Given
    Long cardholderId = 1L;
    String offerType = "Holiday";
    
    Offer offer1 = new Offer();
    offer1.setId(1L);
    offer1.setTitle("Winter Holiday Offer");
    offer1.setOfferType(offerType);
    offer1.setActive(true);
    offer1.setCreatedAt(LocalDateTime.now());

    OfferDTO offerDTO1 = new OfferDTO();
    offerDTO1.setId(offer1.getId());
    offerDTO1.setTitle(offer1.getTitle());
    offerDTO1.setOfferType(offer1.getOfferType());
    offerDTO1.setActive(offer1.getActive());
    
    when(offerRepository.findActiveOffers(any())).thenReturn(List.of(offer1));
    when(offerMapper.toDTO(offer1)).thenReturn(offerDTO1);
    
    // When
    List<OfferDTO> result = offerEligibilityService.filterOffersByOfferType(cardholderId, offerType);
    
    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
    assertEquals(1, result.size());
    assertEquals(offerDTO1.getId(), result.get(0).getId());
    assertEquals(offerDTO1.getTitle(), result.get(0).getTitle());
    assertEquals(offerDTO1.getOfferType(), result.get(0).getOfferType());
    assertNotNull(result.get(0).getActive());
    assertTrue(result.get(0).getActive());
}

    @Test
void shouldGetEligibleOffersForCardholder_nullId() {
    // Given
    Long cardholderId = null;

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> {
        offerEligibilityService.getEligibleOffersForCardholder(cardholderId);
    });
}

    @Test
void shouldGetEligibleOffersForCardholder_emptyActiveCards() {
    // Given
    Long cardholderId = 1L;
    when(cardholderCardRepository.findByCardholderIdAndActiveTrue(cardholderId)).thenReturn(Collections.emptyList());

    // When & Then
    assertThrows(CardholderNotEligibleException.class, () -> offerEligibilityServiceProxy.getEligibleOffersForCardholder(cardholderId));
}

    @Test
void shouldGetEligibleOffersForCardholder_cardholderNotFound() {
    // Given
    Long nonExistingCardholderId = 999L;
    when(cardholderRepository.findByEmail(anyString())).thenReturn(Optional.empty());

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> offerEligibilityServiceProxy.getEligibleOffersForCardholder(nonExistingCardholderId));
    verify(cardholderRepository).findByEmail(anyString());
}

    @Test
void shouldGetEligibleOffersForCardholder_maxActiveCards() {
    // Given
    Long cardholderId = 1L; // assuming this ID corresponds to a cardholder with maximum active cards
    CardholderCard cardholderCard = new CardholderCard();
    cardholderCard.setActive(true);
    List<CardholderCard> activeCards = Arrays.asList(cardholderCard, cardholderCard, cardholderCard); // maximum active cards setup
    when(cardholderCardRepository.findByCardholderIdAndActiveTrue(cardholderId)).thenReturn(activeCards);
    
    OfferDTO offerDTO = new OfferDTO();
    offerDTO.setId(1L);
    offerDTO.setTitle("Exclusive Offer");
    offerDTO.setOfferType("Discount");
    offerDTO.setStartDate(LocalDate.now());
    offerDTO.setEndDate(LocalDate.now().plusDays(10));
    offerDTO.setActive(true);
    List<OfferDTO> eligibleOffers = Arrays.asList(offerDTO);
    when(offerRepository.findActiveOffers(any(LocalDate.class))).thenReturn(new ArrayList<>());
    when(offerRepository.findActiveOffersByMerchant(anyLong())).thenReturn(new ArrayList<>());
    when(offerEligibilityServiceProxy.filterOffersByOfferType(cardholderId, "Discount")).thenReturn(eligibleOffers);

    // When
    EligibleOfferResponseDTO result = offerEligibilityService.getEligibleOffersForCardholder(cardholderId);

    // Then
    assertNotNull(result);
    assertEquals(cardholderId, result.getCardholderId());
    assertEquals(1, result.getEligibleOffers().size());
    assertEquals("Exclusive Offer", result.getEligibleOffers().get(0).getTitle());
    verify(cardholderCardRepository).findByCardholderIdAndActiveTrue(cardholderId);
    verify(offerEligibilityServiceProxy).filterOffersByOfferType(cardholderId, "Discount");
}

    @Test
void shouldGetEligibleOffersForCardholder_securityInjectionAttempt() {
    // Given
    Long injectedCardholderId = Long.valueOf("123 OR 1=1"); // simulated SQL injection
    when(cardholderRepository.findByActiveTrue()).thenReturn(new ArrayList<>());

    // When
    EligibleOfferResponseDTO result = offerEligibilityServiceProxy.getEligibleOffersForCardholder(injectedCardholderId);

    // Then
    assertNotNull(result);
    assertEquals(0, result.getEligibleOffers().size());
    verify(cardholderRepository).findByActiveTrue();
}

}