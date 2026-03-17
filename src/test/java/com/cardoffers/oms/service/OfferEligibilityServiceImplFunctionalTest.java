package com.cardoffers.oms.service;

import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import com.cardoffers.oms.service.OfferEligibilityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import com.cardoffers.oms.model.entity.Merchant;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.cache.type=none",
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration,org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration"
    })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class OfferEligibilityServiceImplFunctionalTest {

    @MockBean
    private CardholderRepository cardholderRepository;

    @MockBean
    private CardholderCardRepository cardholderCardRepository;

    @MockBean
    private OfferRepository offerRepository;

    @MockBean
    private OfferMapper offerMapper;

    @MockBean
    private OfferEligibilityService offerEligibilityServiceProxy;

    @Autowired
    private OfferEligibilityService offerEligibilityService;

    @Test
void shouldFilterOffersByCategory_happyPath() {
    // Given
    Long cardholderId = 1L;
    String category = "electronics";
    Offer offer = new Offer();
    offer.setId(1L);
    offer.setTitle("10% off electronics");
    offer.setActive(true);
    offer.setMerchant(new Merchant()); // Assuming Merchant is created and set properly with required fields
    offer.setCreatedAt(LocalDateTime.now());
    
    List<Offer> offers = new ArrayList<>();
    offers.add(offer);
    
    when(offerRepository.findActiveOffers(any())).thenReturn(offers);
    when(offerMapper.toDTO(any())).thenReturn(new OfferDTO()); // Assuming you set fields in OfferDTO as needed
    
    // When
    List<OfferDTO> result = offerEligibilityService.filterOffersByCategory(cardholderId, category);
    
    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
    assertEquals(1, result.size());
    // Validate specific fields of the first result as necessary
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
    offer1.setDiscountPercentage(BigDecimal.valueOf(15));
    offer1.setActive(true);
    offer1.setCreatedAt(LocalDateTime.now());
    
    OfferDTO expectedOfferDTO = new OfferDTO();
    expectedOfferDTO.setId(1L);
    expectedOfferDTO.setTitle("Offer 1");
    expectedOfferDTO.setOfferType(offerType);
    
    when(offerRepository.findActiveOffers(any())).thenReturn(List.of(offer1));
    when(offerMapper.toDTO(any(Offer.class))).thenReturn(expectedOfferDTO);

    // When
    List<OfferDTO> result = offerEligibilityService.filterOffersByOfferType(cardholderId, offerType);

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
    assertEquals(1, result.size());
    assertEquals(expectedOfferDTO.getId(), result.get(0).getId());
    assertEquals(expectedOfferDTO.getTitle(), result.get(0).getTitle());
    assertEquals(expectedOfferDTO.getOfferType(), result.get(0).getOfferType());
}

    @Test
void shouldGetEligibleOffersForCardholder_nullCardholderId() {
    // Given
    Long cardholderId = null;

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> offerEligibilityService.getEligibleOffersForCardholder(cardholderId));
}

    @Test
void shouldGetEligibleOffersForCardholder_emptyCardholderId() {
    // Given
    Long cardholderId = 0L;

    // When/Then
    assertThrows(ResourceNotFoundException.class, () -> offerEligibilityService.getEligibleOffersForCardholder(cardholderId));
}

    @Test
void shouldGetEligibleOffersForCardholder_noActiveCards() {
    // Given
    Long cardholderId = 123L;
    when(cardholderCardRepository.findByCardholderIdAndActiveTrue(cardholderId)).thenReturn(new ArrayList<>());

    // When & Then
    assertThrows(CardholderNotEligibleException.class, () -> offerEligibilityService.getEligibleOffersForCardholder(cardholderId));
    verify(cardholderCardRepository).findByCardholderIdAndActiveTrue(cardholderId);
}

    @Test
void shouldGetEligibleOffersForCardholder_cardNetworkIdsNull() {
    // Given
    Long cardholderId = 1L;
    List<CardholderCard> activeCards = new ArrayList<>();
    CardholderCard card = new CardholderCard();
    card.setId(1L);
    card.setActive(true);
    card.setCardNetwork(null); // null card network
    activeCards.add(card);
    
    when(cardholderCardRepository.findByCardholderIdAndActiveTrue(cardholderId)).thenReturn(activeCards);
    List<Offer> offers = new ArrayList<>();
    Offer offer = new Offer();
    offer.setId(1L);
    offer.setTitle("Exclusive Offer");
    offer.setActive(true);
    offers.add(offer);
    
    when(offerRepository.findActiveOffers(any())).thenReturn(offers);
    OfferDTO offerDTO = new OfferDTO();
    offerDTO.setId(1L);
    offerDTO.setTitle("Exclusive Offer");
    when(offerMapper.toDTO(any(Offer.class))).thenReturn(offerDTO);
    
    // When
    EligibleOfferResponseDTO response = offerEligibilityService.getEligibleOffersForCardholder(cardholderId);
    
    // Then
    assertNotNull(response);
    assertEquals(cardholderId, response.getCardholderId());
    assertNotNull(response.getEligibleOffers());
    assertTrue(response.getEligibleOffers().isEmpty());
    assertEquals(1, response.getEligibleOffers().size());
    assertEquals("Exclusive Offer", response.getEligibleOffers().get(0).getTitle());
    verify(cardholderCardRepository).findByCardholderIdAndActiveTrue(cardholderId);
    verify(offerRepository).findActiveOffers(any());
    verify(offerMapper).toDTO(any(Offer.class));
}

}