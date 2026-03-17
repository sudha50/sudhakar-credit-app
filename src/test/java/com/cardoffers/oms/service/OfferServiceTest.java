package com.cardoffers.oms.service;

import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import com.cardoffers.oms.model.dto.OfferDTO;
import com.cardoffers.oms.model.dto.OfferSummaryDTO;
import com.cardoffers.oms.service.OfferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.cardoffers.oms.repository.OfferRepository;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import com.cardoffers.oms.model.dto.CardNetworkDTO;
import com.cardoffers.oms.model.dto.MerchantDTO;

@ExtendWith(MockitoExtension.class)
class OfferServiceTest {

    @Mock
    private OfferService offerService;
    @Mock
    private OfferRepository offerRepository;

    @Test
void shouldCreateOffer_happyPath() {
    // Given
    OfferDTO dto = new OfferDTO();
    dto.setTitle("Exclusive Discount");
    dto.setDescription("Get 20% off on your next purchase.");
    dto.setOfferType("Discount");
    dto.setDiscountPercentage(new BigDecimal("20"));
    dto.setMinimumPurchaseAmount(new BigDecimal("100"));
    dto.setStartDate(LocalDate.now());
    dto.setEndDate(LocalDate.now().plusDays(30));
    dto.setSource("Website");
    MerchantDTO merchant = new MerchantDTO();
    merchant.setId(1L); // Assuming an existing merchant ID
    dto.setMerchant(merchant);
    CardNetworkDTO cardNetwork = new CardNetworkDTO();
    cardNetwork.setId(1L); // Assuming an existing card network ID
    dto.setCardNetwork(cardNetwork);
  
    when(offerService.createOffer(dto)).thenReturn(dto);

    // When
    OfferDTO result = offerService.createOffer(dto);

    // Then
    assertNotNull(result);
    assertEquals("Exclusive Discount", result.getTitle());
    assertEquals("Get 20% off on your next purchase.", result.getDescription());
    assertEquals("Discount", result.getOfferType());
    assertEquals(0, new BigDecimal("20").compareTo(result.getDiscountPercentage()));
    assertEquals(0, new BigDecimal("100").compareTo(result.getMinimumPurchaseAmount()));
    result.prePersist();
    assertNotNull(result.getStartDate());
    result.prePersist();
    assertNotNull(result.getEndDate());
    assertEquals("Website", result.getSource());
    assertNotNull(result.getMerchant());
    assertNotNull(result.getCardNetwork());
    verify(offerService).createOffer(dto);
}

    @Test
void shouldUpdateOffer_happyPath() {
    // Given
    OfferDTO updatedOffer = new OfferDTO();
    updatedOffer.setId(1L);
    updatedOffer.setTitle("Updated Offer Title");
    updatedOffer.setOfferType("Discount");
    updatedOffer.setStartDate(LocalDate.now());
    updatedOffer.setEndDate(LocalDate.now().plusDays(10));
    updatedOffer.setSource("Website");
    
    OfferDTO existingOffer = new OfferDTO();
    existingOffer.setId(1L);
    existingOffer.setTitle("Original Offer Title");
    existingOffer.setOfferType("Discount");
    existingOffer.setStartDate(LocalDate.now());
    existingOffer.setEndDate(LocalDate.now().plusDays(10));
    existingOffer.setSource("Website");

    when(offerRepository.findById(1L)).thenReturn(Optional.of(existingOffer));
    when(offerRepository.save(any())).thenReturn(updatedOffer);

    // When
    OfferDTO result = offerService.updateOffer(1L, updatedOffer);

    // Then
    assertNotNull(result);
    assertEquals("Updated Offer Title", result.getTitle());
    assertEquals(1L, result.getId());
    verify(offerRepository).findById(1L);
    verify(offerRepository).save(any());
}

    @Test
void shouldSearchOffers_happyPath() {
    // Given
    OfferDTO offer1 = new OfferDTO();
    offer1.setId(1L);
    offer1.setTitle("Discount on Shoes");
    offer1.setOfferType("Discount");
    offer1.setStartDate(LocalDate.now());
    offer1.setEndDate(LocalDate.now().plusDays(30));
    offer1.setMerchant(new MerchantDTO()); // Assume MerchantDTO is properly set
    offer1.setCardNetwork(new CardNetworkDTO()); // Assume CardNetworkDTO is properly set
    offer1.setSource("Website");
    
    List<OfferDTO> expectedOffers = Collections.singletonList(offer1);
    when(offerService.searchOffers("shoes", "Discount", null)).thenReturn(expectedOffers);

    // When
    List<OfferDTO> result = offerService.searchOffers("shoes", "Discount", null);

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Discount on Shoes", result.get(0).getTitle());
    assertEquals("Discount", result.get(0).getOfferType());
    assertEquals(LocalDate.now(), result.get(0).getStartDate());
    assertEquals(LocalDate.now().plusDays(30), result.get(0).getEndDate());
    verify(offerService).searchOffers("shoes", "Discount", null);
}

    @Test
void shouldGetOfferById_nullId() {
    // Given
    Long offerId = null;

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> offerService.getOfferById(offerId));
}

    @Test
void shouldGetAllActiveOffers_emptyList() {
    // Given
    when(offerService.getAllActiveOffers()).thenReturn(Collections.emptyList());

    // When
    List<OfferDTO> result = offerService.getAllActiveOffers();

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
    verify(offerService).getAllActiveOffers();
}

    @Test
void shouldCreateOffer_nullPayload() {
    // Given
    OfferDTO dto = null;

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> offerService.createOffer(dto));
}



    @Test
void shouldSearchOffers_sqlInjection() {
    // Given
    String sqlInjectionKeyword = "'; DROP TABLE offers; --";
    
    // When
    List<OfferDTO> result = offerService.searchOffers(sqlInjectionKeyword, null, null);

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
}

}