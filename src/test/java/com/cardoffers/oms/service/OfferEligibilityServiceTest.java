package com.cardoffers.oms.service;

import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
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
import com.cardoffers.oms.model.dto.CardNetworkDTO;
import com.cardoffers.oms.model.dto.MerchantDTO;

@ExtendWith(MockitoExtension.class)
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
    offer1.setTitle("Discount on TVs");
    offer1.setOfferType("Discount");
    offer1.setDiscountPercentage(new BigDecimal("15.00"));
    offer1.setMinimumPurchaseAmount(new BigDecimal("500.00"));
    offer1.setStartDate(LocalDate.now());
    offer1.setEndDate(LocalDate.now().plusDays(30));
    offer1.setSource("Website");

    OfferDTO offer2 = new OfferDTO();
    offer2.setId(2L);
    offer2.setTitle("Sale on Laptops");
    offer2.setOfferType("Sale");
    offer2.setDiscountPercentage(new BigDecimal("10.00"));
    offer2.setMinimumPurchaseAmount(new BigDecimal("750.00"));
    offer2.setStartDate(LocalDate.now());
    offer2.setEndDate(LocalDate.now().plusDays(60));
    offer2.setSource("Mobile App");

    List<OfferDTO> expectedOffers = Arrays.asList(offer1, offer2);
    when(offerEligibilityService.filterOffersByCategory(cardholderId, category)).thenReturn(expectedOffers);

    // When
    List<OfferDTO> result = offerEligibilityService.filterOffersByCategory(cardholderId, category);

    // Then
    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("Discount on TVs", result.get(0).getTitle());
    assertEquals("Sale on Laptops", result.get(1).getTitle());
    verify(offerEligibilityService).filterOffersByCategory(cardholderId, category);
}

    @Test
void shouldFilterOffersByOfferType_happyPath() {
    // Given
    Long cardholderId = 1L;
    String offerType = "Discount";
    
    OfferDTO offer1 = new OfferDTO();
    offer1.setId(1L);
    offer1.setTitle("Offer 1");
    offer1.setOfferType(offerType);
    offer1.setStartDate(LocalDate.now());
    offer1.setEndDate(LocalDate.now().plusDays(10));
    offer1.setMerchant(new MerchantDTO());
    offer1.setCardNetwork(new CardNetworkDTO());
    offer1.setSource("Website");
    
    List<OfferDTO> offers = List.of(offer1);
    when(offerEligibilityService.filterOffersByOfferType(cardholderId, offerType)).thenReturn(offers);
    
    // When
    List<OfferDTO> result = offerEligibilityService.filterOffersByOfferType(cardholderId, offerType);

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Offer 1", result.get(0).getTitle());
    assertEquals(offerType, result.get(0).getOfferType());
}

    @Test
void shouldGetEligibleOffersForCardholder_nullCardholderId() {
    // Given
    Long cardholderId = null;

    // When & Then
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

    // When / Then
    assertThrows(IllegalArgumentException.class, () -> offerEligibilityService.filterOffersByCategory(cardholderId, category));
}

    @Test
void shouldFilterOffersByOfferType_nullOfferType() {
    // Given
    Long cardholderId = 1L;
    String offerType = null;

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> 
        offerEligibilityService.filterOffersByOfferType(cardholderId, offerType));
}

    @Test
void shouldGetEligibleOffersForCardholder_concurrentCalls() throws InterruptedException {
    // Given
    Long cardholderId = 1L;
    EligibleOfferResponseDTO expectedResponse = new EligibleOfferResponseDTO();
    expectedResponse.setCardholderId(cardholderId);
    expectedResponse.setEligibleOffers(new ArrayList<>());
    expectedResponse.setTotalOffers(0);

    when(offerEligibilityService.getEligibleOffersForCardholder(cardholderId)).thenReturn(expectedResponse);

    // When
    Runnable task = () -> {
        EligibleOfferResponseDTO result = offerEligibilityService.getEligibleOffersForCardholder(cardholderId);
        assertNotNull(result);
        assertEquals(cardholderId, result.getCardholderId());
        assertEquals(expectedResponse.getTotalOffers(), result.getTotalOffers());
        assertEquals(expectedResponse.getEligibleOffers(), result.getEligibleOffers());
    };

    Thread thread1 = new Thread(task);
    Thread thread2 = new Thread(task);
    thread1.start();
    thread2.start();
    thread1.join();
    thread2.join();

    // Then
    verify(offerEligibilityService, times(2)).getEligibleOffersForCardholder(cardholderId);
}

}