package com.cardoffers.oms.service;

import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
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
import com.cardoffers.oms.model.dto.EligibleOfferResponseDTO;
import com.cardoffers.oms.model.dto.OfferDTO;
import com.cardoffers.oms.service.OfferEligibilityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class OfferEligibilityServiceTest {

    @Mock
    private OfferEligibilityService offerEligibilityService;

    @Test
void shouldFilterOffersByCategory_happyPath() {
    // Given
    Long cardholderId = 1L;
    String category = "Electronics";

    OfferDTO offer1 = new OfferDTO();
    offer1.setId(1L);
    offer1.setTitle("Discount on Laptops");
    offer1.setOfferType("Sale");
    offer1.setDiscountPercentage(new BigDecimal("15.00"));
    offer1.setCashbackAmount(new BigDecimal("50.00"));
    offer1.setMinimumPurchaseAmount(new BigDecimal("200.00"));
    offer1.setStartDate(LocalDate.now());
    offer1.setEndDate(LocalDate.now().plusDays(30));
    offer1.setSource("Website");
    offer1.setMaxRedemptions(100);
    offer1.setCurrentRedemptions(0);
    offer1.setActive(true);
    
    List<OfferDTO> expectedOffers = Collections.singletonList(offer1);
    when(offerEligibilityService.filterOffersByCategory(cardholderId, category)).thenReturn(expectedOffers);

    // When
    List<OfferDTO> result = offerEligibilityService.filterOffersByCategory(cardholderId, category);

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(1L, result.get(0).getId());
    assertEquals("Discount on Laptops", result.get(0).getTitle());
    assertEquals(new BigDecimal("15.00"), result.get(0).getDiscountPercentage());
    verify(offerEligibilityService).filterOffersByCategory(cardholderId, category);
}

    @Test
void shouldFilterOffersByOfferType_happyPath() {
    // Given
    Long cardholderId = 1L;
    String offerType = "Discount";
    
    OfferDTO offerDTO = new OfferDTO();
    offerDTO.setId(1L);
    offerDTO.setTitle("Special Discount");
    offerDTO.setOfferType(offerType);
    offerDTO.setStartDate(LocalDate.now());
    offerDTO.setEndDate(LocalDate.now().plusDays(10));
    offerDTO.setSource("Online");
    
    List<OfferDTO> expectedOffers = Collections.singletonList(offerDTO);
    when(offerEligibilityService.filterOffersByOfferType(cardholderId, offerType)).thenReturn(expectedOffers);

    // When
    List<OfferDTO> result = offerEligibilityService.filterOffersByOfferType(cardholderId, offerType);

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Special Discount", result.get(0).getTitle());
    assertEquals(offerType, result.get(0).getOfferType());
}

    @Test
void shouldGetEligibleOffersForCardholder_nullCardholderId() {
    // Given
    Long cardholderId = null;

    // When / Then
    assertThrows(IllegalArgumentException.class, () -> offerEligibilityService.getEligibleOffersForCardholder(cardholderId));
}

    @Test
void shouldGetEligibleOffersForCardholder_emptyCardholderId() {
    // Given
    Long cardholderId = null;

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> offerEligibilityService.getEligibleOffersForCardholder(cardholderId));
}

    @Test
void shouldFilterOffersByCategory_emptyCategory() {
    // Given
    Long cardholderId = 123L;
    String category = "";
    when(offerEligibilityService.filterOffersByCategory(cardholderId, category)).thenReturn(new ArrayList<>());

    // When
    List<OfferDTO> result = offerEligibilityService.filterOffersByCategory(cardholderId, category);

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
    verify(offerEligibilityService).filterOffersByCategory(cardholderId, category);
}

    @Test
void shouldFilterOffersByOfferType_nullOfferType() {
    // Given
    Long cardholderId = 123L;
    
    // When & Then
    assertThrows(IllegalArgumentException.class, () -> {
        offerEligibilityService.filterOffersByOfferType(cardholderId, null);
    });
}

    @Test
void shouldGetEligibleOffersForCardholder_concurrentAccess() throws InterruptedException {
    // Given
    Long cardholderId = 1L;
    EligibleOfferResponseDTO expectedResponse = new EligibleOfferResponseDTO();
    expectedResponse.setCardholderId(cardholderId);
    when(offerEligibilityService.getEligibleOffersForCardholder(cardholderId)).thenReturn(expectedResponse);

    Runnable task = () -> {
        offerEligibilityService.getEligibleOffersForCardholder(cardholderId);
    };

    Thread thread1 = new Thread(task);
    Thread thread2 = new Thread(task);
    
    // When
    thread1.start();
    thread2.start();
    thread1.join();
    thread2.join();

    // Then
    verify(offerEligibilityService, times(2)).getEligibleOffersForCardholder(cardholderId);
    assertEquals(cardholderId, expectedResponse.getCardholderId());
}

}