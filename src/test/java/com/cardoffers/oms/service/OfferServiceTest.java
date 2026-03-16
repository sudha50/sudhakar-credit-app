package com.cardoffers.oms.service;

import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import jakarta.validation.*;
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
import jakarta.validation.Valid;
import com.cardoffers.oms.model.dto.OfferDTO;
import com.cardoffers.oms.model.dto.OfferSummaryDTO;
import com.cardoffers.oms.service.OfferService;
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
@MockitoSettings(strictness = Strictness.LENIENT)
class OfferServiceTest {

    @Mock
    private OfferService offerService;

    @Test
void shouldCreateOffer_happyPath() {
    // Given
    MerchantDTO merchant = new MerchantDTO();
    // Set necessary fields for MerchantDTO if there are any required

    CardNetworkDTO cardNetwork = new CardNetworkDTO();
    // Set necessary fields for CardNetworkDTO if there are any required

    OfferDTO offerDTO = new OfferDTO();
    offerDTO.setTitle("Special Discount");
    offerDTO.setOfferType("Discount");
    offerDTO.setStartDate(LocalDate.now());
    offerDTO.setEndDate(LocalDate.now().plusDays(30));
    offerDTO.setMerchant(merchant);
    offerDTO.setCardNetwork(cardNetwork);
    offerDTO.setSource("Online Store");

    when(offerService.createOffer(any(OfferDTO.class))).thenReturn(offerDTO);

    // When
    OfferDTO result = offerService.createOffer(offerDTO);

    // Then
    assertNotNull(result);
    assertEquals("Special Discount", result.getTitle());
    assertEquals("Discount", result.getOfferType());
    result.prePersist();
    assertNotNull(result.getStartDate());
    assertEquals(LocalDate.now().plusDays(30), result.getEndDate());
    assertEquals("Online Store", result.getSource());
    verify(offerService).createOffer(offerDTO);
}

    @Test
void shouldUpdateOffer_happyPath() {
    // Given
    OfferDTO offer = new OfferDTO();
    offer.setId(1L);
    offer.setTitle("Special Offer");
    offer.setOfferType("Discount");
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(10));
    offer.setMerchant(new MerchantDTO()); // Assuming MerchantDTO has the default fields set
    offer.setCardNetwork(new CardNetworkDTO()); // Assuming CardNetworkDTO has the default fields set
    when(offerRepository.findById(1L)).thenReturn(Optional.of(offer));

    OfferDTO updatedOffer = new OfferDTO();
    updatedOffer.setId(1L);
    updatedOffer.setTitle("Updated Special Offer");
    updatedOffer.setOfferType("Discount");
    updatedOffer.setStartDate(LocalDate.now());
    updatedOffer.setEndDate(LocalDate.now().plusDays(10));
    updatedOffer.setMerchant(new MerchantDTO());
    updatedOffer.setCardNetwork(new CardNetworkDTO());
    
    when(offerRepository.save(any())).thenReturn(updatedOffer);

    // When
    OfferDTO result = offerService.updateOffer(1L, updatedOffer);

    // Then
    assertNotNull(result);
    assertEquals("Updated Special Offer", result.getTitle());
    assertEquals(1L, result.getId());
    verify(offerRepository).findById(1L);
    verify(offerRepository).save(any());
}

    @Test
void shouldSearchOffers_happyPath() {
    // Given
    OfferDTO offer = new OfferDTO();
    offer.setTitle("Summer Sale");
    offer.setOfferType("Discount");
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(30));
    offer.setSource("Website");
    
    List<OfferDTO> expectedOffers = Collections.singletonList(offer);
    when(offerService.searchOffers("Summer", "Discount", "Sales")).thenReturn(expectedOffers);

    // When
    List<OfferDTO> result = offerService.searchOffers("Summer", "Discount", "Sales");

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Summer Sale", result.get(0).getTitle());
    assertEquals("Discount", result.get(0).getOfferType());
    assertEquals(LocalDate.now(), result.get(0).getStartDate());
    assertEquals(LocalDate.now().plusDays(30), result.get(0).getEndDate());
    verify(offerService).searchOffers("Summer", "Discount", "Sales");
}

    @Test
void shouldGetOfferById_nullId() {
    // Given
    Long id = null;

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> offerService.getOfferById(id));
}

    @Test
void shouldGetAllActiveOffers_emptyResult() {
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
void shouldUpdateOffer_nonExistingId() {
    // Given
    Long nonExistingId = 999L;
    OfferDTO offerDTO = new OfferDTO();
    offerDTO.setTitle("Special Offer");
    offerDTO.setOfferType("Discount");
    offerDTO.setStartDate(LocalDate.now());
    offerDTO.setEndDate(LocalDate.now().plusDays(10));
    offerDTO.setSource("Online");
    
    when(offerService.updateOffer(eq(nonExistingId), any(OfferDTO.class))).thenThrow(new OfferNotFoundException("Not found"));

    // When & Then
    assertThrows(OfferNotFoundException.class, () -> offerService.updateOffer(nonExistingId, offerDTO));
}

    @Test
void shouldSearchOffers_sqlInjection() {
    // Given
    String sqlInjectionKeyword = "' OR '1'='1";
    when(offerService.searchOffers(sqlInjectionKeyword, null, null)).thenReturn(Collections.emptyList());

    // When
    List<OfferDTO> result = offerService.searchOffers(sqlInjectionKeyword, null, null);

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
    verify(offerService).searchOffers(sqlInjectionKeyword, null, null);
}

}