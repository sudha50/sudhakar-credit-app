package com.cardoffers.oms.service;

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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.*;
import com.cardoffers.oms.model.dto.CardNetworkDTO;
import com.cardoffers.oms.model.dto.MerchantDTO;


@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.cache.type=none",
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration,org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration"
    })
class OfferServiceFunctionalTest {

    @Autowired
    private OfferService offerService;

    @Test
void shouldCreateOffer_happyPath() {
    // Given
    MerchantDTO merchant = new MerchantDTO();
    merchant.setId(1L); // assuming an ID is set
    CardNetworkDTO cardNetwork = new CardNetworkDTO();
    cardNetwork.setId(1L); // assuming an ID is set

    OfferDTO offerDTO = new OfferDTO();
    offerDTO.setTitle("Special Offer");
    offerDTO.setOfferType("Discount");
    offerDTO.setStartDate(LocalDate.now());
    offerDTO.setEndDate(LocalDate.now().plusDays(30));
    offerDTO.setMerchant(merchant);
    offerDTO.setCardNetwork(cardNetwork);
    offerDTO.setSource("Online");


    // When
    OfferDTO result = offerService.createOffer(offerDTO);

    // Then
    assertNotNull(result);
    assertEquals("Special Offer", result.getTitle());
    assertEquals("Discount", result.getOfferType());
    result.prePersist();
    assertNotNull(result.getStartDate());
    assertEquals(LocalDate.now().plusDays(30), result.getEndDate());
    assertEquals("Online", result.getSource());
}

    @Test
void shouldUpdateOffer_happyPath() {
    // Given
    OfferDTO existingOffer = new OfferDTO();
    existingOffer.setId(1L);
    existingOffer.setTitle("Old Title");
    existingOffer.setOfferType("Discount");
    existingOffer.setStartDate(LocalDate.now());
    existingOffer.setEndDate(LocalDate.now().plusDays(10));
    MerchantDTO merchant = new MerchantDTO();
    merchant.setId(1L);
    existingOffer.setMerchant(merchant);
    CardNetworkDTO cardNetwork = new CardNetworkDTO();
    cardNetwork.setId(1L);
    existingOffer.setCardNetwork(cardNetwork);
    
    OfferDTO updatedOffer = new OfferDTO();
    updatedOffer.setId(1L);
    updatedOffer.setTitle("Updated Title");
    updatedOffer.setOfferType("Discount");
    updatedOffer.setStartDate(LocalDate.now());
    updatedOffer.setEndDate(LocalDate.now().plusDays(10));
    updatedOffer.setMerchant(merchant);
    updatedOffer.setCardNetwork(cardNetwork);


    // When
    OfferDTO result = offerService.updateOffer(1L, updatedOffer);

    // Then
    assertNotNull(result);
    assertEquals("Updated Title", result.getTitle());
}

    @Test
void shouldSearchOffers_happyPath() {
    // Given
    MerchantDTO merchant = new MerchantDTO();
    merchant.setName("Test Merchant");
    // Assume a valid merchant id is generated or fetched
    CardNetworkDTO cardNetwork = new CardNetworkDTO();
    cardNetwork.setName("Test Network");
    // Assume a valid card network id is generated or fetched

    OfferDTO offer = new OfferDTO();
    offer.setTitle("Test Offer");
    offer.setOfferType("Discount");
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(10));
    offer.setMerchant(merchant);
    offer.setCardNetwork(cardNetwork);
    offer.setSource("Web");
    
    List<OfferDTO> expectedOffers = Collections.singletonList(offer);
    

    // When
    List<OfferDTO> result = offerService.searchOffers("Test", "Discount", "Retail");

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Test Offer", result.get(0).getTitle());
    assertEquals("Discount", result.get(0).getOfferType());
    assertEquals("Web", result.get(0).getSource());
}

    @Test
void shouldGetOfferById_nullId() {
    // Given
    Long id = null;

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> offerService.getOfferById(id));
}

    @Test
void shouldGetAllActiveOffers_emptyList() throws Exception {
    // Given

    // When
    List<OfferDTO> result = offerService.getAllActiveOffers();

    // Then
    assertNotNull(result);
    assertTrue(result.isEmpty());
}

    @Test
void shouldCreateOffer_missingFields() {
    // Given
    OfferDTO offerDTO = new OfferDTO();
    // Missing required fields like title and offerType
    offerDTO.setDescription("A special offer");
    offerDTO.setStartDate(LocalDate.now());
    offerDTO.setEndDate(LocalDate.now().plusDays(10));
    
    // When & Then
    mockMvc.perform(post("/offers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(offerDTO)))
            .andExpect(status().isInternalServerError());
}

}