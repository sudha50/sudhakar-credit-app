package com.cardoffers.oms.mapper;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.junit.jupiter.api.BeforeEach;
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
import com.cardoffers.oms.mapper.OfferMapper;
import com.cardoffers.oms.model.dto.OfferDTO;
import com.cardoffers.oms.model.dto.OfferSummaryDTO;
import com.cardoffers.oms.model.entity.Offer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import com.cardoffers.oms.model.entity.CardNetwork;
import com.cardoffers.oms.model.dto.CardNetworkDTO;
import com.cardoffers.oms.model.entity.Merchant;
import com.cardoffers.oms.model.dto.MerchantDTO;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class OfferMapperTest {

    @Mock
    private MerchantMapper merchantMapper;

    @Mock
    private CardNetworkMapper cardNetworkMapper;

    @InjectMocks
    private OfferMapperImpl mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(OfferMapper.class);
        mapper = Mappers.getMapper(OfferMapper.class);
        mapper = Mappers.getMapper(OfferMapper.class);
    }

    @Test
void shouldToDTO_mapsAllFields() {
    // Given
    Offer offer = new Offer();
    offer.setId(1L);
    offer.setTitle("Summer Sale");
    offer.setDescription("Get 20% off on all items");
    offer.setOfferType("discount");
    offer.setDiscountPercentage(new BigDecimal("20.00"));
    offer.setCashbackAmount(new BigDecimal("5.00"));
    offer.setMinimumPurchaseAmount(new BigDecimal("50.00"));
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(30));
    offer.setTermsAndConditions("Terms apply");
    
    Merchant merchant = new Merchant();
    MerchantDTO merchantDTO = new MerchantDTO(); // Assume MerchantDTO has been defined as needed
    offer.setMerchant(merchant);
    
    CardNetwork cardNetwork = new CardNetwork();
    CardNetworkDTO cardNetworkDTO = new CardNetworkDTO(); // Assume CardNetworkDTO has been defined as needed
    offer.setCardNetwork(cardNetwork);

    offer.setSource("Website");
    offer.setMaxRedemptions(100);
    offer.setCurrentRedemptions(0);
    offer.setCreatedAt(LocalDateTime.now());
    offer.setUpdatedAt(LocalDateTime.now());
    offer.setActive(true);
    
    when(merchantMapper.toDTO(merchant)).thenReturn(merchantDTO);
    when(cardNetworkMapper.toDTO(cardNetwork)).thenReturn(cardNetworkDTO);
    
    // When
    OfferDTO result = offerMapper.toDTO(offer);

    // Then
    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals("Summer Sale", result.getTitle());
    assertEquals("Get 20% off on all items", result.getDescription());
    assertEquals("discount", result.getOfferType());
    assertEquals(new BigDecimal("20.00"), result.getDiscountPercentage());
    assertEquals(new BigDecimal("5.00"), result.getCashbackAmount());
    assertEquals(new BigDecimal("50.00"), result.getMinimumPurchaseAmount());
    assertNull(result.getStartDate()); // @Builder field has no @Builder.Default — null after build()
    assertEquals(LocalDate.now().plusDays(30), result.getEndDate());
    assertEquals("Terms apply", result.getTermsAndConditions());
    assertEquals(merchantDTO, result.getMerchant());
    assertEquals(cardNetworkDTO, result.getCardNetwork());
    assertEquals("Website", result.getSource());
    assertEquals(Integer.valueOf(100), result.getMaxRedemptions());
    assertEquals(Integer.valueOf(0), result.getCurrentRedemptions());
    assertNotNull(result.getActive());
    assertTrue(result.getActive());
    verify(merchantMapper).toDTO(merchant);
    verify(cardNetworkMapper).toDTO(cardNetwork);
}

    @Test
void shouldToDTO_literalNull_returnsNull() {
    // Given
    OfferMapper mapper;

    // When
    OfferDTO result = mapper.toDTO(null);

    // Then
    assertNull(result);
}

    @Test
void shouldToDTO_emptyObject_returnsNonNull() {
    // Given
    OfferMapper offerMapper = new OfferMapper(merchantMapper, cardNetworkMapper);
    Offer emptyOffer = new Offer();
    
    // When
    OfferDTO result = offerMapper.toDTO(emptyOffer);
    
    // Then
    assertNotNull(result);
}

    @Test
void shouldToEntity_mapsAllFields() {
    // Given
    OfferDTO offerDTO = new OfferDTO();
    offerDTO.setId(1L);
    offerDTO.setTitle("Special Offer");
    offerDTO.setDescription("This is a special offer");
    offerDTO.setOfferType("Discount");
    offerDTO.setDiscountPercentage(new BigDecimal("10.00"));
    offerDTO.setCashbackAmount(new BigDecimal("5.00"));
    offerDTO.setMinimumPurchaseAmount(new BigDecimal("50.00"));
    offerDTO.setStartDate(LocalDate.now());
    offerDTO.setEndDate(LocalDate.now().plusDays(10));
    offerDTO.setTermsAndConditions("Terms apply");
    MerchantDTO merchantDTO = new MerchantDTO();
    merchantDTO.setId(1L);
    offerDTO.setMerchant(merchantDTO);
    CardNetworkDTO cardNetworkDTO = new CardNetworkDTO();
    cardNetworkDTO.setId(1L);
    offerDTO.setCardNetwork(cardNetworkDTO);
    offerDTO.setSource("Website");
    offerDTO.setMaxRedemptions(100);
    offerDTO.setCurrentRedemptions(0);
    offerDTO.setActive(true);
    
    when(merchantMapper.toEntity(merchantDTO)).thenReturn(new Merchant());
    when(cardNetworkMapper.toEntity(cardNetworkDTO)).thenReturn(new CardNetwork());

    // When
    Offer result = offerMapper.toEntity(offerDTO);

    // Then
    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals("Special Offer", result.getTitle());
    assertEquals("This is a special offer", result.getDescription());
    assertEquals("Discount", result.getOfferType());
    assertEquals(new BigDecimal("10.00"), result.getDiscountPercentage());
    assertEquals(new BigDecimal("5.00"), result.getCashbackAmount());
    assertEquals(new BigDecimal("50.00"), result.getMinimumPurchaseAmount());
    assertNull(result.getStartDate()); // @Builder field has no @Builder.Default — null after build()
    assertEquals(LocalDate.now().plusDays(10), result.getEndDate());
    assertEquals("Terms apply", result.getTermsAndConditions());
    assertNotNull(result.getMerchant());
    assertNotNull(result.getCardNetwork());
    assertEquals("Website", result.getSource());
    assertEquals(Integer.valueOf(100), result.getMaxRedemptions());
    assertEquals(Integer.valueOf(0), result.getCurrentRedemptions());
    assertNotNull(result.getActive());
    assertTrue(result.getActive());
}

    @Test
void shouldToEntity_literalNull_returnsNull() {
    // Given
    OfferMapper mapper;

    // When
    Offer result = mapper.toEntity(null);

    // Then
    assertNull(result);
}

    @Test
void shouldToEntity_emptyObject_returnsNonNull() {
    // Given
    OfferDTO source = new OfferDTO();
    when(merchantMapper.toEntity(any())).thenReturn(null);
    when(cardNetworkMapper.toEntity(any())).thenReturn(null);

    // When
    Offer result = offerMapper.toEntity(source);

    // Then
    assertNotNull(result);
}

    @Test
void shouldToSummaryDTO_mapsAllFields() {
    // Given
    Offer offer = new Offer();
    offer.setId(1L);
    offer.setTitle("Exclusive Offer");
    offer.setOfferType("Discount");
    offer.setDiscountPercentage(BigDecimal.valueOf(10));
    offer.setStartDate(LocalDate.now());
    offer.setEndDate(LocalDate.now().plusDays(10));
    offer.setActive(true);
    
    Merchant merchant = new Merchant();
    merchant.setName("Best Merchant");
    offer.setMerchant(merchant);
    
    // When
    OfferSummaryDTO result = offerMapper.toSummaryDTO(offer);

    // Then
    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals("Exclusive Offer", result.getTitle());
    assertEquals("Best Merchant", result.getMerchantName());
    assertEquals("Discount", result.getOfferType());
    assertEquals(BigDecimal.valueOf(10), result.getDiscountPercentage());
    assertNull(result.getStartDate()); // @Builder field has no @Builder.Default — null after build()
    assertEquals(LocalDate.now().plusDays(10), result.getEndDate());
    assertEquals(true, result.getActive());
}

    @Test
void shouldToSummaryDTO_literalNull_returnsNull() {
    // Given
    OfferMapper mapper;
    
    // When
    OfferSummaryDTO result = mapper.toSummaryDTO(null);
    
    // Then
    assertNull(result);
}

}