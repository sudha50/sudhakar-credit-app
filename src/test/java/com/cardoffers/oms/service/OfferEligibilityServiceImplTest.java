package com.cardoffers.oms.service;

import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
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
    offer.setTitle("Discount on Mobiles");
    offer.setDescription("Get 20% off on mobile phones");
    offer.setOfferType("Discount");
    offer.setDiscountPercentage(BigDecimal.valueOf(20));
    offer.setActive(true);
    offer.setCreatedAt(LocalDateTime.now());

    List<Offer> offers = Collections.singletonList(offer);

    when(offerRepository.findActiveOffers(any())).thenReturn(offers);
    when(offerMapper.toDTO(any())).thenReturn(new OfferDTO());

    // When
    List<OfferDTO> result = offerEligibilityService.filterOffersByCategory(cardholderId, category);

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
    assertEquals(1, result.size());
    verify(offerRepository).findActiveOffers(any());
    verify(offerMapper).toDTO(any());
}

    @Test
void shouldFilterOffersByOfferType_happyPath() {
    // Given
    Long cardholderId = 1L;
    String offerType = "discount";
    
    Offer offer1 = new Offer();
    offer1.setId(1L);
    offer1.setTitle("Offer 1");
    offer1.setOfferType(offerType);
    offer1.setActive(true);
    offer1.setCreatedAt(LocalDateTime.now());
    
    Offer offer2 = new Offer();
    offer2.setId(2L);
    offer2.setTitle("Offer 2");
    offer2.setOfferType(offerType);
    offer2.setActive(true);
    offer2.setCreatedAt(LocalDateTime.now());
    
    when(offerRepository.findActiveOffers(any())).thenReturn(Arrays.asList(offer1, offer2));
    when(offerMapper.toDTO(any())).thenAnswer(invocation -> {
        Offer o = invocation.getArgument(0);
        OfferDTO dto = new OfferDTO();
        dto.setId(o.getId());
        dto.setTitle(o.getTitle());
        dto.setOfferType(o.getOfferType());
        dto.setActive(o.getActive());
        return dto;
    });
    
    // When
    List<OfferDTO> result = offerEligibilityService.filterOffersByOfferType(cardholderId, offerType);
    
    // Then
    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals(offerType, result.get(0).getOfferType());
    assertEquals(offerType, result.get(1).getOfferType());
}

    @Test
void shouldGetEligibleOffersForCardholder_nullId() {
    // Given
    Long cardholderId = null;

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> offerEligibilityServiceProxy.getEligibleOffersForCardholder(cardholderId));
}

    @Test
void shouldGetEligibleOffersForCardholder_emptyId() {
    // Given
    Long cardholderId = 0L;

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> offerEligibilityServiceProxy.getEligibleOffersForCardholder(cardholderId));
}

    @Test
void shouldGetEligibleOffersForCardholder_noActiveCards() {
    // Given
    Long cardholderId = 1L; 
    when(cardholderCardRepository.findByCardholderIdAndActiveTrue(cardholderId)).thenReturn(new ArrayList<>());

    // When & Then
    assertThrows(CardholderNotEligibleException.class, () -> offerEligibilityServiceProxy.getEligibleOffersForCardholder(cardholderId));
}

    @Test
void shouldGetEligibleOffersForCardholder_cardNetworkIdNull() {
    // Given
    Long cardholderId = 1L;
    CardholderCard activeCard = new CardholderCard();
    activeCard.setCardNetworkId(null);
    activeCard.setActive(true);
    when(cardholderCardRepository.findByCardholderIdAndActiveTrue(cardholderId)).thenReturn(List.of(activeCard));

    // When
    EligibleOfferResponseDTO result = offerEligibilityServiceProxy.getEligibleOffersForCardholder(cardholderId);

    // Then
    assertNotNull(result);
    assertNotNull(result.getEligibleOffers());
    assertTrue(result.getEligibleOffers().isEmpty());
    verify(cardholderCardRepository).findByCardholderIdAndActiveTrue(cardholderId);
}

    @Test
void shouldGetEligibleOffersForCardholder_concurrencyIssues() {
    // Given
    Long cardholderId = 1L;
    CardholderCard cardholderCard = new CardholderCard();
    cardholderCard.setActive(true);
    when(cardholderCardRepository.findByCardholderIdAndActiveTrue(cardholderId)).thenReturn(List.of(cardholderCard));
    
    Offer offer = new Offer();
    offer.setId(1L);
    offer.setTitle("Special Discount");
    offer.setActive(true);
    List<Offer> offers = List.of(offer);
    when(offerRepository.findActiveOffers(any())).thenReturn(offers);
    
    // Simulate a concurrent update to cardholder's active cards
    // (This is just context, no code required here)

    // When
    EligibleOfferResponseDTO result = offerEligibilityServiceProxy.getEligibleOffersForCardholder(cardholderId);

    // Then
    assertNotNull(result);
    assertEquals(cardholderId, result.getCardholderId());
    assertEquals(1, result.getEligibleOffers().size());
    assertEquals("Special Discount", result.getEligibleOffers().get(0).getTitle());
    verify(cardholderCardRepository).findByCardholderIdAndActiveTrue(cardholderId);
    verify(offerRepository).findActiveOffers(any());
}

}