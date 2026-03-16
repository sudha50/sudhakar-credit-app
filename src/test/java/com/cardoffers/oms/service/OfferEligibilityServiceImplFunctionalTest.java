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
import static org.junit.jupiter.api.Assertions.assertNull;
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

@SpringBootTest(exclude = {RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class, CacheAutoConfiguration.class})
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.cache.type=none"})
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
    String category = "Electronics";

    Offer offer = new Offer();
    offer.setId(1L);
    offer.setTitle("Discount on TVs");
    offer.setActive(true);
    offer.setCreatedAt(LocalDateTime.now());
    offer.setMerchant(new Merchant("Best Electronics"));

    List<Offer> offers = Collections.singletonList(offer);
    when(offerRepository.findActiveOffers(any())).thenReturn(offers);
    when(offerMapper.toDTO(any())).thenReturn(new OfferDTO());

    // When
    List<OfferDTO> result = offerEligibilityService.filterOffersByCategory(cardholderId, category);

    // Then
    assertEquals(1, result.size());
    assertNotNull(result.get(0).getActive());
    assertTrue(result.get(0).getActive());
    verify(offerRepository).findActiveOffers(any());
    verify(offerMapper).toDTO(any());
}

    @Test
void shouldFilterOffersByOfferType_happyPath() {
    // Given
    Long cardholderId = 1L;
    String offerType = "Discount";
    Offer offer = new Offer();
    offer.setId(1L);
    offer.setTitle("Special Discount Offer");
    offer.setOfferType(offerType);
    offer.setActive(true);
    offer.setCreatedAt(LocalDateTime.now());
    
    List<Offer> offers = Collections.singletonList(offer);
    
    when(offerRepository.findActiveOffers(any())).thenReturn(offers);
    when(offerMapper.toDTO(any())).thenAnswer(invocation -> {
        Offer entity = invocation.getArgument(0);
        OfferDTO dto = new OfferDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setOfferType(entity.getOfferType());
        dto.setActive(entity.getActive());
        return dto;
    });

    // When
    List<OfferDTO> result = offerEligibilityService.filterOffersByOfferType(cardholderId, offerType);

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
    assertEquals(1, result.size());
    assertEquals(1L, result.get(0).getId());
    assertEquals("Special Discount Offer", result.get(0).getTitle());
    assertEquals(offerType, result.get(0).getOfferType());
    assertNotNull(result.get(0).getActive());
    assertTrue(result.get(0).getActive());
}

    @Test
void shouldGetEligibleOffersForCardholder_nullId() {
    // Given
    Long cardholderId = null;

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> offerEligibilityService.getEligibleOffersForCardholder(cardholderId));
}

    @Test
void shouldGetEligibleOffersForCardholder_emptyId() {
    // Given
    Long cardholderId = 0L;

    // When / Then
    assertThrows(ResourceNotFoundException.class, () -> offerEligibilityService.getEligibleOffersForCardholder(cardholderId));
}

    @Test
void shouldGetEligibleOffersForCardholder_noActiveCards() {
    // Given
    Long cardholderId = 1L;
    when(cardholderCardRepository.findByCardholderIdAndActiveTrue(cardholderId)).thenReturn(Collections.emptyList());

    // When & Then
    assertThrows(CardholderNotEligibleException.class, () -> offerEligibilityService.getEligibleOffersForCardholder(cardholderId));
    verify(cardholderCardRepository).findByCardholderIdAndActiveTrue(cardholderId);
}

    @Test
void shouldGetEligibleOffersForCardholder_cardNetworkMissing() {
    // Given
    Long cardholderId = 1L;
    CardholderCard cardholderCard = new CardholderCard();
    cardholderCard.setActive(true);
    when(cardholderCardRepository.findByCardholderIdAndActiveTrue(cardholderId)).thenReturn(Collections.singletonList(cardholderCard));
    
    Offer offer = new Offer();
    offer.setId(1L);
    offer.setTitle("Offer Title");
    offer.setCardNetwork(null); // Card network is missing
    
    when(offerRepository.findActiveOffers(any())).thenReturn(Collections.singletonList(offer));
    
    // When
    EligibleOfferResponseDTO result = offerEligibilityService.getEligibleOffersForCardholder(cardholderId);
    
    // Then
    assertNotNull(result);
    assertEquals(0, result.getEligibleOffers().size());
    assertNull(result.getCardholderId());
    assertNull(result.getCardholderName());
    verify(offerRepository).findActiveOffers(any());
}

}